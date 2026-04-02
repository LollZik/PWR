import java.util.Iterator;

public class CollatzIterator implements Iterator<Integer> {
    private int currentNumber;

    public CollatzIterator(int startingNumber){
        if(startingNumber<=0){
            throw new IllegalArgumentException("Podaj liczbę większą od 0!");
        }
        this.currentNumber = startingNumber;
    }
    @Override
    public boolean hasNext(){
        return true;
    }

    @Override
    public Integer next(){
        if (currentNumber % 2 == 0){
            currentNumber /= 2;
        }
        else{
            currentNumber = 3*currentNumber +1;
        }
        return currentNumber;
    }
}
