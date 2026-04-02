import java.util.ArrayList;
import java.util.List;

public class StatisicsCollector{
    private int size;
    private double totalLoadSum = 0;
    private int loadSamples = 0;

    private int loadQueryCount = 0;
    private int migrationCount = 0;

    private final ArrayList<Integer> loads = new ArrayList<>();

    public void recordCpuLoads(List<CPU> cpus){
        int sum = 0;
        for (CPU cpu : cpus) {
            int usage = cpu.getUsage();
            loads.add(usage);
            sum += usage;
        }
        totalLoadSum += (double) sum / cpus.size();
        loadSamples++;
    }

    public void incrementQueries() {
        loadQueryCount++;
    }

    public void incrementMigrations() {
        migrationCount++;
    }

    public void setSize(int s){
        this.size = s;
    }

    public double[] getStats(){
        double avgLoad = totalLoadSum / loadSamples;

        // average Deviation
        double avfDev = 0.0;
        for (int load : loads) {
            avfDev += Math.abs(load - avgLoad);
        }
        avfDev = (avfDev / loads.size());
        return new double[]{this.size*1.0,avgLoad,avfDev,this.loadQueryCount*1.0,this.migrationCount*1.0};
    }
}
