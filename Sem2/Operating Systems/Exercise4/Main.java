import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main {
    public final static int PROCESSES_AMOUNT = 10;
    public final static int MIN_PAGE_NUM = 25;
    public final static int MAX_PAGE_NUM = 100;
    public final static int FRAME_SIZE = 100;
    public final static int SIMULATIONS = 25;
    // 100 ramek
    // steer: lower: 0.15, upper: 0.4, deltaT: 50
    // wss:   deltaT: 150,  c: 80
    // Thrashing - minimum 25/40 Page fault'ow

    public static void main(String[] args){
        try{
            DataExporter a = new DataExporter("equal.csv");
            DataExporter b = new DataExporter("proportional.csv");
            DataExporter c = new DataExporter("steer.csv");
            DataExporter d = new DataExporter("wss.csv");
            for(int j = 0; j< SIMULATIONS; j++){
                List<Process> processes = new ArrayList<>();
                //processes.add(new Process(11, Edgecase(11)));
                for (int i = 0; i < PROCESSES_AMOUNT; i++) {
                    processes.add(new Process(i, generatePages(i)));
                }
                Algorithms alg = new Algorithms(FRAME_SIZE, processes,a,b,c,d);

                try{
                alg.EQUAL();
                alg.exportResults("equal.csv");

                alg.PROPORTIONAL();
                alg.exportResults("proportional.csv");

                alg.STEER(0.15, 0.4, 50);
                alg.exportResults("steer.csv");

                alg.WSS(150, 80);
                alg.exportResults("wss.csv");
                }
                catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            a.close();
            b.close();
            c.close();
            d.close();
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static List<Page> generatePages(int processId){
        Random random = new Random();
        int pages = random.nextInt(MIN_PAGE_NUM, MAX_PAGE_NUM);
        int amount = random.nextInt(150,250) * pages;
        List<Page> references = Generator.generatePages(amount, pages, processId);
        return new ArrayList<>(references);
    }

    private static List<Page> Edgecase(int processId){
        int pages = 200;
        int amount = 500 * pages;
        List<Page> references = Generator.generatePages(amount, pages, processId);
        return new ArrayList<>(references);
    }
}
