import java.util.Iterator;
import java.util.ArrayList;
import java.util.Random;
import java.util.Collections;

public class RandomIterator<E> implements Iterator<E> {
    private final E[] array;
    private Random random = new Random();
    private ArrayList<E> shuffledList = new ArrayList<>();
    private int index = 0;

    public RandomIterator(E[] array){

        this.array = array;

        if(array != null){
            for(E elem : array){
                shuffledList.add(elem);
            }
            for(int i = 0; i < shuffledList.size(); i++){
                int swapIndex = random.nextInt(0,shuffledList.size()-1);
                Collections.swap(shuffledList, i, swapIndex);
            }
        }
    }

    @Override
    public boolean hasNext() {
        return (array != null && index < array.length);
    }

    @Override
    public E next() {
        E temp = shuffledList.get(index);
        index++;
        return temp;
    }
}