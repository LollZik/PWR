import java.util.Iterator;
import java.util.ArrayList;

public class SubswapIterator<E> implements Iterator<E> {
    private final Iterator<E> iterator;
    private final int k;
    private ArrayList<E> list;
    private int indexList;

    public SubswapIterator(Iterable<E> iterable, int k){
        this.iterator = iterable.iterator();
        this.k = k;
        this.list = new ArrayList<>();
        this.indexList = 0;
    }

    @Override
    public boolean hasNext() {
        if(iterator.hasNext() || !list.isEmpty()){
            return true;
        }
        return false;
    }

    @Override
    public E next() {
        if(list.isEmpty()){
            indexList = 0;
        }
        while(iterator.hasNext() && indexList<k){
            indexList++;
            list.add(iterator.next());
        }
        if(!list.isEmpty()){
            E temp = list.get(list.size()-1);
            list.remove(list.size()-1);
            return temp;
        }
        return null;
    }

}