public class StatisticsCollector {
    private int initialSize;
    private int pageFaults;
    private int thrashingCount;
    private long totalThrashingDuration;
    private long longestThrashingDuration;

    public StatisticsCollector(){

    }

    // Dodatkowe metryki dla wstrzymań procesów
    private int suspendCount;
    private long totalSuspendDuration;
    private long longestSuspendDuration;

 public void record(int initialSize,
                       int pageFaults,
                       int thrashingCount,
                       long totalThrashingDuration,
                       long longestThrashingDuration) {
        this.initialSize = initialSize;
        this.pageFaults = pageFaults;
        this.thrashingCount = thrashingCount;
        this.totalThrashingDuration = totalThrashingDuration;
        this.longestThrashingDuration = longestThrashingDuration;
    }

    // Przeciążona metoda dla wstrzymań procesów

    public void record(int initialSize,
                       int pageFaults,
                       int thrashingCount,
                       long totalThrashingDuration,
                       long longestThrashingDuration,
                       int suspendCount,
                       long totalSuspendDuration,
                       long longestSuspendDuration) {
        this.initialSize = initialSize;
        this.pageFaults = pageFaults;
        this.thrashingCount = thrashingCount;
        this.totalThrashingDuration = totalThrashingDuration;
        this.longestThrashingDuration = longestThrashingDuration;
        this.suspendCount = suspendCount;
        this.totalSuspendDuration = totalSuspendDuration;
        this.longestSuspendDuration = longestSuspendDuration;
    }

    public void reset() {
        this.initialSize = 0;
        this.pageFaults = 0;
        this.thrashingCount = 0;
        this.totalThrashingDuration = 0;
        this.longestThrashingDuration = 0;
        this.suspendCount = 0;
        this.totalSuspendDuration = 0;
        this.longestSuspendDuration = 0;
    }

    public int getInitialSize() {
        return initialSize;
    }

    public int getPageFaults() {
        return pageFaults;
    }

    public int getThrashingCount() {
        return thrashingCount;
    }

    public long getTotalThrashingDuration() {
        return totalThrashingDuration;
    }

    public long getLongestThrashingDuration() {
        return longestThrashingDuration;
    }

    public int getSuspendCount() {
        return suspendCount;
    }

    public long getTotalSuspendDuration() {
        return totalSuspendDuration;
    }

    public long getLongestSuspendDuration() {
        return longestSuspendDuration;
    }
}