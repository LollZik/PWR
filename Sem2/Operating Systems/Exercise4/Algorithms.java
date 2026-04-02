import java.util.*;
import java.util.stream.Collectors;

public class Algorithms{
    private final int FRAME_SIZE;
    private final List<Process> processes;
    private final ThrashingDetector thrashingDetector;
    private final StatisticsCollector eqStatsCollector;
    private final StatisticsCollector propStatsCollector;
    private final StatisticsCollector steerStatsCollector;
    private final StatisticsCollector wssStatsCollector;
    private final DataExporter a;
    private final DataExporter b;
    private final DataExporter c;
    private final DataExporter d;
    private final List<Page> globalReferences;
    private final Random random = new Random();

    public Algorithms(int FRAME_SIZE, List<Process> processes, DataExporter name1, DataExporter name2, DataExporter name3, DataExporter name4){
        this.FRAME_SIZE = FRAME_SIZE;
        this.processes = processes;
        this.thrashingDetector = new ThrashingDetector(40, 25);
        this.eqStatsCollector = new StatisticsCollector();
        this.propStatsCollector = new StatisticsCollector();
        this.steerStatsCollector = new StatisticsCollector();
        this.wssStatsCollector = new StatisticsCollector();
        this.globalReferences = generateGlobalReferences();
        this.a = name1;
        this.b = name2;
        this.c = name3;
        this.d = name4;
    }

    private List<Page> generateGlobalReferences(){
        List<Page> references = new ArrayList<>();
        Map<Process, Integer> nextIndex = new HashMap<>();
        for (Process p : processes) {
            nextIndex.put(p, 0);
        }

        // dopóki któryś proces ma jeszcze strony do dodania
        while ( nextIndex.entrySet().stream()
                .anyMatch
                        (e -> e.getValue() < e.getKey().getPages().size())){
// Powtarzaj dopóki istnieje proces,
// z którego nie dodano wszystkich odwołań do globalnego ciągu
            for (Process p : processes) {
                int idx = nextIndex.get(p);
                int total = p.getPages().size();
                // jeśli wyczerpano wszystkie strony, pomijamy
                if (idx >= total) continue;

                // 60% szans na dodanie kolejnej strony
                if (random.nextDouble() < 0.6) {
                    Page next = p.getPages().get(idx);
                    references.add(next);
                    nextIndex.put(p, idx + 1);
                }
                // w pozostałych 40%: pomijamy (nie zmieniamy idx)
            }
        }
        return references;
    }

    public void EQUAL(){
        eqStatsCollector.reset();
        thrashingDetector.reset();

        // równy przydział ramek
        int framesPerProcess = FRAME_SIZE / processes.size();
        for (Process p : processes) {
            p.setFrame(framesPerProcess);
        }

        int totalFaults = LRU(globalReferences, processes, thrashingDetector);

        eqStatsCollector.record(
                globalReferences.size(),
                totalFaults,
                thrashingDetector.getThrashingCount(),
                thrashingDetector.getTotalThrashingDurationCalls(),
                thrashingDetector.getLongestThrashingDurationCalls()
        );
    }

    public void PROPORTIONAL(){
        propStatsCollector.reset();
        thrashingDetector.reset();

        // Obliczamy liczbe unikalnych stron
        Map<Integer,Integer> uniqueCount = new HashMap<>();
        for (Process p : processes) {
            int pid = p.getProcessId();
            Set<Integer> uniq = globalReferences.stream()
                    .filter(pg -> pg.getProcesID() == pid)
                    .map(Page::getNr)
                    .collect(Collectors.toSet());
            uniqueCount.put(pid, uniq.size());
        }

        int totalUniquePages = uniqueCount.values().stream()
                .mapToInt(i -> i)
                .sum();

        // Przydział ramek: FRAME_SIZE * s_i / totalUniquePages, minimum 3
        for (Process p : processes) {
            int si = uniqueCount.getOrDefault(p.getProcessId(), 0);
            // jeśli totalUniquePages==0 (brak referencji) — dajemy po równo
            double ratio = totalUniquePages > 0
                    ? (double) si / totalUniquePages
                    : 1.0 / processes.size();
            int fi = (int) Math.ceil(ratio * FRAME_SIZE);
            p.setFrame(Math.max(fi, 3));
        }

        int totalFaults = LRU(globalReferences, processes, thrashingDetector);

        propStatsCollector.record(
                globalReferences.size(),
                totalFaults,
                thrashingDetector.getThrashingCount(),
                thrashingDetector.getTotalThrashingDurationCalls(),
                thrashingDetector.getLongestThrashingDurationCalls()
        );
    }

    public void STEER(double lowerThreshold, double upperThreshold, int deltaT) {
        steerStatsCollector.reset();
        int totalFaults = 0;
        int freeFrames = FRAME_SIZE;

        final double suspendThreshold = 0.96;
        final int resumeInterval = 50;
        final int minResumeFrames = 3;

        Map<Integer, LinkedHashSet<Page>> frameSets   = new HashMap<>();
        Map<Integer, Integer>          pfCount        = new HashMap<>();
        Map<Integer, Integer>          localTick      = new HashMap<>();
        Map<Integer, Boolean>          isSuspended    = new HashMap<>();
        Map<Integer, Integer>          suspendDurations = new HashMap<>();
        Set<Integer>                   suspendedPIDs  = new LinkedHashSet<>();

        // Inicjalny podział ramek (proporcjonalnie)
        int totalDistinct = processes.stream()
                .flatMap(p -> p.getPages().stream())
                .map(pg -> pg.nr)
                .distinct().toList().size();

        for (Process p : processes) {
            long uniq = p.getPages().stream().map(pg -> pg.nr).distinct().count();
            int f = Math.max(3, (int)Math.ceil((double)uniq / totalDistinct * FRAME_SIZE));
            p.setFrame(f);
            freeFrames -= f;

            frameSets.put(p.getProcessId(), new LinkedHashSet<>());
            pfCount.put(p.getProcessId(), 0);
            localTick.put(p.getProcessId(), 0);
            isSuspended.put(p.getProcessId(), false);
            suspendDurations.put(p.getProcessId(), 0);
        }

        List<Page> postponedPages = new ArrayList<>();
        int globalTick = 0;

        // Główna pętla
        for (Page pg : globalReferences) {
            globalTick++;
            int pid = pg.getProcesID();
            Process proc = processes.stream()
                    .filter(p -> p.getProcessId() == pid)
                    .findFirst().orElseThrow();

            // Jeśli zawieszony → dodaj do postPoned i licz czas suspend'u
            if (isSuspended.get(pid)) {
                postponedPages.add(pg);
                suspendDurations.put(pid, suspendDurations.get(pid) + 1);
            } else {
                LinkedHashSet<Page> frame = frameSets.get(pid);
                int capacity = proc.getFrameCount();

                boolean pageFault = !frame.contains(pg);
                if (pageFault) {
                    if (frame.size() >= capacity) {
                        Iterator<Page> it = frame.iterator();
                        it.next();
                        it.remove();
                    }
                    totalFaults++;
                    pfCount.put(pid, pfCount.get(pid) + 1);
                } else {
                    frame.remove(pg);
                }
                frame.add(pg);

                thrashingDetector.record(pageFault);

                localTick.put(pid, localTick.get(pid) + 1);

                // co deltaT mierz pff
                if (localTick.get(pid) >= deltaT){
                    double ppf = (double)pfCount.get(pid) / deltaT;

                    if (ppf > suspendThreshold){
                        // zawieszamy
                        proc.setFrame(0);
                        isSuspended.put(pid, true);
                        suspendedPIDs.add(pid);
                        freeFrames += capacity;
                    }
                    else if (ppf > upperThreshold && freeFrames > 0){
                        // zabieramy ramke
                        proc.setFrame(capacity + 1);
                        freeFrames--;
                    }
                    else if (ppf < lowerThreshold && capacity > 3) {
                        proc.setFrame(capacity - 1);
                        freeFrames++;
                        // przycinamy ramkę
                        LinkedHashSet<Page> fset = frameSets.get(pid);
                        while (fset.size() > proc.getFrameCount()) {
                            Iterator<Page> it = fset.iterator();
                            it.next();
                            it.remove();
                        }
                    }

                    // reset okna
                    pfCount.put(pid, 0);
                    localTick.put(pid, 0);
                }
            }

            // Co resumeInterval spróbuj wznowić zawieszony proces
            if (globalTick % resumeInterval == 0 && !suspendedPIDs.isEmpty() && freeFrames >= minResumeFrames) {
                Iterator<Integer> it = suspendedPIDs.iterator();
                int toResume = it.next();
                it.remove();

                Process pr = processes.stream()
                        .filter(p -> p.getProcessId() == toResume)
                        .findFirst().orElseThrow();

                pr.setFrame(minResumeFrames);
                freeFrames -= minResumeFrames;
                isSuspended.put(toResume, false);
                // od teraz jego wczesniej wstrzymane odwołania zostaną obsłużone na końcu
            }
        }

        // Obsługa odwołań dla zawieszonych procesów
        for (int pid : new ArrayList<>(suspendedPIDs)) {
            Process proc = processes.stream()
                    .filter(p -> p.getProcessId() == pid)
                    .findFirst().orElseThrow();

            LinkedHashSet<Page> frame = frameSets.get(pid);
            int capacity = proc.getFrameCount();
            if (capacity == 0) continue;

            for (Page pg : postponedPages) {
                if (pg.getProcesID() != pid) continue;
                boolean pageFault = !frame.contains(pg);
                if (pageFault) {
                    if (frame.size() >= capacity) {
                        Iterator<Page> it = frame.iterator();
                        it.next();
                        it.remove();
                    }
                    totalFaults++;
                } else {
                    frame.remove(pg);
                }
                frame.add(pg);
                thrashingDetector.record(pageFault);
            }
        }

        // Statystyki
        int suspendCount = suspendDurations.entrySet().stream()
                .mapToInt(e -> e.getValue() > 0 ? 1 : 0).sum();
        long totalSuspendDuration = suspendDurations.values().stream().mapToLong(i -> i).sum();
        long longestSuspendDuration = suspendDurations.values().stream().mapToLong(i -> i).max().orElse(0);

        steerStatsCollector.record(
                globalReferences.size(),
                totalFaults,
                thrashingDetector.getThrashingCount(),
                thrashingDetector.getTotalThrashingDurationCalls(),
                thrashingDetector.getLongestThrashingDurationCalls(),
                suspendCount,
                totalSuspendDuration,
                longestSuspendDuration
        );
    }




    public void WSS(int deltaT, int c){
        wssStatsCollector.reset();
        thrashingDetector.reset();

        int totalFaults = 0;
        int globalTick = 0;

        // Równy, początkowy podział ramek
        int n = processes.size();
        int freeFrames = FRAME_SIZE;
        int equalShare = FRAME_SIZE / n;
        for (Process p : processes) {
            p.setFrame(equalShare);
            freeFrames -= equalShare;
        }

        Map<Integer, Deque<Integer>>      windows      = new HashMap<>();
        Map<Integer, LinkedHashSet<Page>> frameSets    = new HashMap<>();
        Map<Integer, Boolean>             isSuspended  = new HashMap<>();
        Map<Integer, Integer>             suspendStart = new HashMap<>();
        Map<Integer, Integer>             suspendCount = new HashMap<>();
        Map<Integer, Long>                suspendDur   = new HashMap<>();
        List<Page>                        postponed    = new ArrayList<>();

        for (Process p : processes) {
            int pid = p.getProcessId();
            windows     .put(pid, new ArrayDeque<>());
            frameSets   .put(pid, new LinkedHashSet<>());
            isSuspended .put(pid, false);
            suspendStart.put(pid, -1);
            suspendCount.put(pid, 0);
            suspendDur  .put(pid, 0L);
        }

        for (Page pg : globalReferences) {
            globalTick++;
            int pid = pg.getProcesID();

            Deque<Integer> win = windows.get(pid);
            win.addLast(pg.getNr());
            if (win.size() > deltaT) win.removeFirst();

            // LRU
            if (!isSuspended.get(pid)) {
                LinkedHashSet<Page> frame = frameSets.get(pid);
                int capacity = processes.stream()
                        .filter(x -> x.getProcessId() == pid)
                        .findFirst().orElseThrow()
                        .getFrameCount();

                boolean pageFault = !frame.contains(pg);
                if (pageFault) {
                    if (frame.size() >= capacity) {
                        frame.removeFirst();
                    }
                    totalFaults++;
                } else {
                    frame.remove(pg);
                }
                frame.add(pg);
                thrashingDetector.record(pageFault);
            } else {
                // Zapisujemy odwołania zawieszonych procesów
                postponed.add(pg);
            }

            //  Co c odwołań obliczamy WSS
            Map<Integer,Integer> wss = null;
            if (globalTick % c == 0){
                wss = new HashMap<>();
                for (Process q : processes) {
                    int qid = q.getProcessId();
                    wss.put(qid, new HashSet<>(windows.get(qid)).size());
                }
            }

            // Co delta t odwołań zarządzamy ramkami i opcjonalnie odmrażamy kilka procesów
            if (globalTick % deltaT == 0 && wss != null){
                // Obliczamy D
                int D = wss.entrySet().stream()
                        .filter(e -> !isSuspended.get(e.getKey()))
                        .mapToInt(Map.Entry::getValue)
                        .sum();

                // Zawieszamy procesy o największym WSS aż D <= FRAME_SIZE
                while (D > FRAME_SIZE){
                    int toSuspend = wss.entrySet().stream()
                            .filter(e -> !isSuspended.get(e.getKey()))
                            .max(Map.Entry.comparingByValue())
                            .get().getKey();

                    Process sp = processes.stream()
                            .filter(p -> p.getProcessId() == toSuspend)
                            .findFirst().orElseThrow();
                    int oldFrames = sp.getFrameCount();

                    sp.setFrame(0);
                    isSuspended.put(toSuspend, true);
                    suspendCount.put(toSuspend, suspendCount.get(toSuspend) + 1);
                    suspendStart.put(toSuspend, globalTick);

                    freeFrames += oldFrames;
                    D -= wss.get(toSuspend);
                }

                // Proporcjonalny przydział ramek procesom aktywnym
                int used = 0;
                for (Process q : processes) {
                    int qid = q.getProcessId();
                    if (isSuspended.get(qid)) {
                        q.setFrame(0);
                    } else {
                        int wi = wss.getOrDefault(qid, 0);
                        // ustawiamy procesowi WSS_i ramek, o ile to jest > 3
                        int fi = Math.max(wi, 3);
                        q.setFrame(fi);
                        used += fi;
                    }
                }
                freeFrames = FRAME_SIZE - used;

                // Odmrażamy kilka procesów: freeFrames % 8 najdłużej zawieszonych
                int toUnfreeze = freeFrames % 8;
                if (toUnfreeze > 0) {
                    // sortujemy zawieszonych po czasie zawieszenia rosnąco
                    List<Integer> suspendedList = processes.stream()
                            .map(Process::getProcessId)
                            .filter(isSuspended::get)
                            .sorted(Comparator.comparingInt(suspendStart::get))
                            .toList();

                    for (int i = 0; i < toUnfreeze && i < suspendedList.size(); i++) {
                        int qid = suspendedList.get(i);
                        Process pr = processes.stream()
                                .filter(p -> p.getProcessId() == qid)
                                .findFirst().orElseThrow();

                        long dur = (long)globalTick - suspendStart.get(qid);
                        suspendDur.put(qid, suspendDur.get(qid) + dur);

                        // odmrażamy i przydzielamy wszystkie dostępne ramki
                        pr.setFrame(freeFrames);
                        isSuspended.put(qid, false);
                        suspendStart.put(qid, -1);

                        freeFrames = 0;
                    }
                }
            }
        }

        // Po pętli dokańczamy zamrożone procesy – równy podział pozostałych ramek
        List<Integer> stillSuspended = processes.stream()
                .map(Process::getProcessId)
                .filter(isSuspended::get)
                .toList();

        if (!stillSuspended.isEmpty() && freeFrames > 0) {
            int perProc = freeFrames / stillSuspended.size();
            for (int qid : stillSuspended) {
                Process pr = processes.stream()
                        .filter(p -> p.getProcessId() == qid)
                        .findFirst().orElseThrow();

                // dokańczamy ich odwołania
                LinkedHashSet<Page> frame = frameSets.get(qid);
                pr.setFrame(perProc);
                for (Page pg : postponed) {
                    if (pg.getProcesID() != qid) continue;
                    boolean pf = !frame.contains(pg);
                    if (pf) {
                        if (frame.size() >= pr.getFrameCount()) {
                            Iterator<Page> it = frame.iterator();
                            it.next(); it.remove();
                        }
                        totalFaults++;
                    } else {
                        frame.remove(pg);
                    }
                    frame.add(pg);
                    thrashingDetector.record(pf);
                }
            }
        }

        // Zapis statystyk suspenow
        for (int qid : stillSuspended) {
            long dur = (long)globalTick - suspendStart.get(qid);
            suspendDur.put(qid, suspendDur.get(qid) + dur);
        }

        int totalSuspendCount     = suspendCount.values().stream().mapToInt(i -> i).sum();
        long totalSuspendDuration = suspendDur.values().stream().mapToLong(l -> l).sum();
        long longestSuspend       = suspendDur.values().stream().mapToLong(l -> l).max().orElse(0L);

        wssStatsCollector.record(
                globalReferences.size(),
                totalFaults,
                thrashingDetector.getThrashingCount(),
                thrashingDetector.getTotalThrashingDurationCalls(),
                thrashingDetector.getLongestThrashingDurationCalls(),
                totalSuspendCount,
                totalSuspendDuration,
                longestSuspend
        );
    }




    /**
     * @param references globalna lista odwołań do stron
     * @param processes  lista wszystkich procesów
     * @param detector - detektor thrashingu (będzie rejestrował każde wywołanie)
     * @return łączna liczba page-faults we wszystkich procesach
     */
    private int LRU(List<Page> references, List<Process> processes, ThrashingDetector detector) {
        // Mapa processId -> jego frame
        Map<Integer, LinkedHashSet<Page>> frameSet = new HashMap<>();
        for (Process p : processes) {
            frameSet.put(p.getProcessId(), new LinkedHashSet<>());
        }

        int totalFaults = 0;

        for (Page pg : references) {
            int pageID = pg.procesID;
            LinkedHashSet<Page> frame = frameSet.get(pageID);

            // Pobieramy aktualny limit ramek
            int capacity = processes.stream()
                    .filter(p -> p.getProcessId() == pageID)
                    .findFirst()
                    .orElseThrow()
                    .getFrameCount();

            // Sprawdzenie czy nastapil page fault
            boolean pageFault = !frame.contains(pg);
            if (pageFault){
                if (frame.size() >= capacity) {
                    Iterator<Page> it = frame.iterator();
                    it.next();
                    it.remove();
                }
                totalFaults++;
              } else {
                // odświeżamy pozycję strony w ramce
                frame.remove(pg);
            }
            frame.add(pg);
            detector.record(pageFault);
        }

        return totalFaults;
    }


    public void exportResults(String filename) throws Exception {
        switch(filename){
            case "equal.csv":
                this.a.exportToCSV(eqStatsCollector);
                break;

            case "proportional.csv":
                this.b.exportToCSV(propStatsCollector);
                break;
            case "steer.csv":
                this.c.exportToCSV(steerStatsCollector);
                break;
            default:
                this.d.exportToCSV(wssStatsCollector);
                break;

        }
    }
}
