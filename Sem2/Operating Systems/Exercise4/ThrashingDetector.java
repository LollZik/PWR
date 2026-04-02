import java.util.LinkedList;

public class ThrashingDetector {
    private final int windowSize;                // deltaT
    private final int pageFaultThreshold;        // próg błędów stron w oknie

    private final LinkedList<Boolean> history = new LinkedList<>();
    private int tickCount = 0;

    private boolean inThrashing = false;
    private int thrashingCount = 0;
    private int currentThrashingWindows = 0;
    private long totalThrashingDurationCalls = 0;
    private long longestThrashingDurationCalls = 0;

    /**
     * @param windowSize liczba wywołań, po której następuje sprawdzenie (deltaT)
     * @param pageFaultThreshold minimalna liczba page faults w oknie, by uznać thrashing
     */
    public ThrashingDetector(int windowSize, int pageFaultThreshold) {
        this.windowSize = windowSize;
        this.pageFaultThreshold = pageFaultThreshold;
    }

    /**
     * Rejestruje jedno wywołanie
     * Co windowSize wywołań sprawdza, czy thrashing jest aktywny.
     * @param pageFault czy w tym odwołaniu do strony był page fault
     * @return true jeśli w aktualnym oknie wykryto thrashing
     */
    public boolean record(boolean pageFault){
        history.add(pageFault);
        if (history.size() > windowSize) {
            history.removeFirst();
        }
        tickCount++;

        // Co deltaT wywołań sprawdzamy czy wystąpił thrashing
        if (tickCount % windowSize != 0){
            return inThrashing;
        }

        int faultsInWindow = 0;
        for (boolean b : history){
            if (b) faultsInWindow++;
        }

        boolean thrashNow = (faultsInWindow >= pageFaultThreshold);
        if (thrashNow){
            // jeśli dopiero zaczynamy thrashing
            if (!inThrashing) {
                inThrashing = true;
                thrashingCount++;
                currentThrashingWindows = 1;
            } else {
                // kontynuacja thrashingu
                currentThrashingWindows++;
            }
        }else{
            // jeśli kończymy thrashing
            if (inThrashing) {
                inThrashing = false;
                long durationCalls = (long) currentThrashingWindows * windowSize;
                totalThrashingDurationCalls += durationCalls;
                longestThrashingDurationCalls = Math.max(longestThrashingDurationCalls, durationCalls);
                currentThrashingWindows = 0;
            }
        }

        return inThrashing;
    }

    public int getThrashingCount() {
        return thrashingCount;
    }

    public long getTotalThrashingDurationCalls() {
        return totalThrashingDurationCalls;
    }

    public long getLongestThrashingDurationCalls() {
        return longestThrashingDurationCalls;
    }

    public void reset() {
        history.clear();
        tickCount = 0;
        inThrashing = false;
        thrashingCount = 0;
        currentThrashingWindows = 0;
        totalThrashingDurationCalls = 0;
        longestThrashingDurationCalls = 0;
    }
}