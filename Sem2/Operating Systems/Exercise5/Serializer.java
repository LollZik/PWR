import java.io.BufferedWriter;
import java.io.FileWriter;

public class Serializer {
    private final String name;
    private BufferedWriter writer;

    public Serializer(String name){
        this.name = name;
    }
    public void writerInit(String header){

        String filePath = this.name+".txt";
        try{
            this.writer = new BufferedWriter(new FileWriter(filePath));
            writer.write(header+"\n");
        }
        catch (Exception e) {
            throw new RuntimeException("Błąd inicjalizacji writer'a");
        }
    }
    public void write(String text){
        try{
            writer.write(text+"\n");
        }
        catch (Exception e){
            throw new RuntimeException("Błąd przy dopisywaniu tekstu do writer'a");
        }
    }

    public void close(){
        try{
            if(writer != null){
                writer.close();
            }
        }
        catch (Exception e){
            throw new RuntimeException("Nie zamknięto writer'a");
        }
    }

    public void export(StatisicsCollector stat){
        double[] x = stat.getStats();
        String text = String.format("%d;%.2f;%.2f;%d;%d",(int)x[0],x[1],x[2],(int)x[3],(int)x[4]);
        write(text);
    }
}
