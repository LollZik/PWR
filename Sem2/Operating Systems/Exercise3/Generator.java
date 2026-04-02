import java.util.*;

public class Generator {
    private static final Random random = new Random();

    public static int[] generate(int length, int totalPages) {
        int[] result = new int[length];
        int i = 0;

        while (i < length) {
            int remaining = length - i;

            int phaseLength = Math.min(random.nextInt(length/20, length/10), remaining);


            int mode = random.nextInt(0, 11);
            // 0 - all pages, 1-5: random pages, 6-10:  contiguous pages

           int localPagesCount;
            if(mode <5){
                localPagesCount = totalPages;
            }
            else{
                localPagesCount = random.nextInt(3, (totalPages/2)+1);
            }
            List<Integer> workingSet;
            if(mode == 0){
                workingSet = getAllPages(totalPages);
            }
            else if(mode < 6){
                workingSet = getRandomPages(localPagesCount, totalPages);
            }
            else{
                workingSet = getContiguousPages(localPagesCount, totalPages);
            }

            for (int j = 0; j < phaseLength && i < length; j++, i++) {
                result[i] = workingSet.get(random.nextInt(workingSet.size()));
            }
        }
        return result;
    }

    private static List<Integer> getAllPages(int totalPages) {
        List<Integer> pages = new ArrayList<>();
        for (int i = 0; i < totalPages; i++) {
            pages.add(i);
        }
        return pages;
    }

    private static List<Integer> getRandomPages(int count, int totalPages) {
        Set<Integer> set = new HashSet<>();
        while (set.size() < count) {
            set.add(random.nextInt(totalPages));
        }
        return new ArrayList<>(set);
    }

    private static List<Integer> getContiguousPages(int count, int totalPages) {
        int maxStart = totalPages - count;
        int start = (maxStart > 0) ? random.nextInt(1, maxStart + 1) : 0;

        List<Integer> pages = new ArrayList<>();
        for (int i = 0; i < count && start + i < totalPages; i++) {
            pages.add(start + i);
        }
        return pages;
    }
}