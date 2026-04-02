import java.util.*;

public class Generator {
    private static final Random random = new Random();

    /**
     * @param length      liczba odwołań do wygenerowania
     * @param totalPages  maksymalna wartość numeru strony (k)
     * @param processId   ID procesu
     * @return lista odwołań jako obiekty Page
     */
    public static List<Page> generatePages(int length, int totalPages, int processId) {
        List<Page> result = new ArrayList<>(length);
        int i = 0;

        while (i < length) {
            int remaining = length - i;
            int phaseLen = Math.min(
                    random.nextInt(Math.max(1, length/20), Math.max(2, length/10)),
                    remaining
            );

            int mode = random.nextInt(11);
            // jeśli mode==0 → wszystkie strony, 1–5 → przypadkowy podzbiór, 6–10 → ciągły podzbiór
            int localCount = (mode < 5)
                    ? totalPages
                    : random.nextInt(3, totalPages/2 + 1);

            List<Integer> workingIndices;
            if (mode == 0) {
                workingIndices = getAllIndices(totalPages);
            } else if (mode < 6) {
                workingIndices = getRandomIndices(localCount, totalPages);
            } else {
                workingIndices = getContiguousIndices(localCount, totalPages);
            }

            // dodajemy do wyniku kolejne odwołania z tego zestawu
            for (int j = 0; j < phaseLen && i < length; j++, i++) {
                int pageNr = workingIndices.get(random.nextInt(workingIndices.size()));
                result.add(new Page(pageNr, 0, processId));
            }
        }

        return result;
    }

    private static List<Integer> getAllIndices(int totalPages) {
        List<Integer> idx = new ArrayList<>(totalPages);
        for (int i = 0; i < totalPages; i++) idx.add(i);
        return idx;
    }

    private static List<Integer> getRandomIndices(int count, int totalPages) {
        Set<Integer> set = new HashSet<>();
        while (set.size() < Math.min(count, totalPages)) {
            set.add(random.nextInt(totalPages));
        }
        return new ArrayList<>(set);
    }

    private static List<Integer> getContiguousIndices(int count, int totalPages) {
        int maxStart = Math.max(0, totalPages - count);
        int start = (maxStart > 0)
                ? random.nextInt(0, maxStart + 1)
                : 0;
        List<Integer> idx = new ArrayList<>(count);
        for (int i = 0; i < count && start + i < totalPages; i++) {
            idx.add(start + i);
        }
        return idx;
    }
}
