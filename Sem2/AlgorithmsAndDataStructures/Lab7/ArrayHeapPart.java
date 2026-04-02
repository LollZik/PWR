import java.util.ArrayList;
import MyComparator.*;

public class ArrayHeapPart<T> extends HeapPart<T>{
    private final MyComparator<? super T> comparator;
    private ArrayList<T> heap;

    public ArrayHeapPart(MyComparator<? super T> comparator){
        this.comparator = comparator;
        this.heap = new ArrayList<>();
    }

    private void swap(int left, int right){
        T temp = heap.get(left);
        heap.set(left, heap.get(right));
        heap.set(right, temp);
    }

    private void sink(int idx, int n){
        int idxOfBigger=2*idx+1;
        if(idxOfBigger<n){
            if(idxOfBigger+1<n &&
                    comparator.compare(heap.get(idxOfBigger), heap.get(idxOfBigger+1))<0)
                idxOfBigger++;
            if(comparator.compare(heap.get(idx), heap.get(idxOfBigger))<0){
                swap(idx,idxOfBigger);
                sink(idxOfBigger,n);
            }
        }
    }

    public void heapAdjustment(int n){
        for(int i = (n-1)/2; i >= 0; i--)
            sink(i, n);
    }

    public T get(int index){
        return heap.get(index);
    }
    public void set(int index, T value){
        heap.set(index, value);
    }

    public void remove(){
        heap.clear();
    }
    public int heapSize(){
        return heap.size();
    }
    public void add(T value){
        heap.add(value);
    }
}