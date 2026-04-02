import java.util.ArrayList;
import java.util.List;

public class ThrashingTracker {
    private final int deltaT;
    private final int thresholdN;
    private final List<Boolean> pageFaults;

    private int totalThrashingCount;
    private int maxThrashingDuration;
    private int totalThrashingDuration;

    public ThrashingTracker(int deltaT, int thresholdN) {
        this.deltaT = deltaT;
        this.thresholdN = thresholdN;
        this.pageFaults = new ArrayList<>();
    }

    public void addPageFault(boolean isPageFault) {
        pageFaults.add(isPageFault);
    }


    public void analyzeThrashing() {
        totalThrashingCount = 0;
        maxThrashingDuration = 0;
        totalThrashingDuration = 0;

        int i = 0;
        while (i <= pageFaults.size() - deltaT) {
            int faultCount = 0;

            for (int j = 0; j < deltaT; j++) {
                if (pageFaults.get(i + j)) {
                    faultCount++;
                }
            }

            if (faultCount >= thresholdN) {
                totalThrashingCount++;

                int duration = deltaT;
                int j = i + deltaT;

                while (j < pageFaults.size() && pageFaults.get(j)) {
                    duration++;
                    j++;
                }

                maxThrashingDuration = Math.max(maxThrashingDuration, duration);
                totalThrashingDuration += duration;

                i = j;
            } else {
                i++;
            }
        }
    }

    public int getTotalThrashingCount() {
        return totalThrashingCount;
    }

    public int getMaxThrashingDuration() {
        return maxThrashingDuration;
    }

    public int getTotalThrashingDuration() {
        return totalThrashingDuration;
    }
}
