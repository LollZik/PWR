import core.AbstractSwappingSortingAlgorithm;

import java.util.Comparator;
import java.util.List;
import java.util.LinkedList;

public class Mergesort<T> extends AbstractSwappingSortingAlgorithm<T> {
    public Mergesort(Comparator<? super T> comparator){
        super(comparator);
    }

    public List<T> sort(List<T> list) {
        return mergesort(list, 0, list.size() - 1);
    }


    private List<T> mergesort(List<T> list, int startIndex, int endIndex) {

        if(startIndex >= endIndex) {
            List<T> result = new LinkedList<>();
            if(startIndex == endIndex){
                result.addFirst(list.get(startIndex));
                return result;
            }
            return (new LinkedList<>());
        }
        else if(startIndex == endIndex - 1){
            return merge(mergesort(list,startIndex,startIndex),mergesort(list,endIndex,endIndex));
        }
        int mid1 = startIndex + (endIndex - startIndex) / 3;
        int mid2 = startIndex + 2*(endIndex - startIndex) / 3;
        return merge(
                merge(mergesort(list, startIndex, mid1), mergesort(list, mid1 + 1, mid2)),
                mergesort(list, mid2+1,endIndex));
    }

    private List<T> merge(List<T> left, List<T> right) {
        List<T> result = new LinkedList<>();
        T l;
        T r;
        while(!left.isEmpty() && !right.isEmpty()){
            l = left.removeFirst();
            r = right.removeFirst();
            if(compare(l, r) <= 0){
                result.addLast(l);
            }
            else{
                result.add(r);
            }
        }
        while(!left.isEmpty()){
            result.add(left.removeFirst());
        }
        while(!right.isEmpty()){
            result.add(right.removeFirst());
        }
        return result;
    }
}