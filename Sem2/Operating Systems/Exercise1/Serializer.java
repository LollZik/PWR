import java.io.BufferedWriter;
import java.io.FileWriter;

public class Serializer {
    private String name;
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
}
