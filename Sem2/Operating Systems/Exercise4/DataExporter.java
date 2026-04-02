import java.io.*;

public class DataExporter {
    private BufferedWriter writer;
    private String filename;

    public DataExporter(String filename) throws IOException{
        this.filename = filename;
        try{
        init();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void exportToCSV(StatisticsCollector results) throws IOException {
        try{
                this.writer.write(String.format("%d,%d,%d,%d,%d,%d,%d,%d\n",
                        results.getInitialSize(),
                        results.getPageFaults(),
                        results.getThrashingCount(),
                        results.getTotalThrashingDuration(),
                        results.getLongestThrashingDuration(),
                        results.getSuspendCount(),
                        results.getTotalSuspendDuration(),
                        results.getLongestSuspendDuration()
                ));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void init() throws IOException{
        this.writer = new BufferedWriter(new FileWriter(this.filename));
        try{
            writer.write("InitialSize,PageFaults,ThrashingCount,TotalThrashing,LongestThrashing,SuspendCount,SuspentDuration,LongestSuspend\n");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void close(){
        try{
        this.writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
