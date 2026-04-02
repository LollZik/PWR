import java.util.Iterator;

public class CollatzIterable implements Iterable<Integer>{
    private int startingNumber;

    public CollatzIterable(int startingNumber){
        if(startingNumber<=0){
            throw new IllegalArgumentException("Podaj liczbę większą od 0!");
        }
        this.startingNumber = startingNumber;
    }
public Iterator<Integer> iterator(){
    return new CollatzIterator(startingNumber);
}


}
