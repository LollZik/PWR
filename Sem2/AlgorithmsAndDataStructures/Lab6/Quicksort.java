import core.AbstractSwappingSortingAlgorithm;
import pivot.PivotSelector;

import java.util.Comparator;
import java.util.List;
import java.util.ListIterator;

public class Quicksort<T> extends AbstractSwappingSortingAlgorithm<T> {
    private final PivotSelector<T> pivotSelector;

    public Quicksort(Comparator<? super T> comparator, PivotSelector<T> pivotSelector){
        super(comparator);
        this.pivotSelector = pivotSelector;
    }

    public List<T> sort(List<T> list){
        if (list.size() > 1) {
            quicksort(list, 0, list.size() - 1);
        }
        return list;
    }

    private void quicksort(List<T> list,int  start, int end){
        if(start >= end){
            return;
        }
        int pivotIndex = partition(list, start, end);

        quicksort(list, start, pivotIndex-1);
        quicksort(list, pivotIndex+1, end);
    }

    private int partition(List<T> list, int start, int end){
        int pivotIndex = pivotSelector.selectPivot(list, start, end);
        T pivot = list.get(pivotIndex);
        swap(list, pivotIndex, end);

        ListIterator<T> leftIt = list.listIterator(start);
        ListIterator<T> rightIt = list.listIterator(end);

        int i = start;
        int j = end-1;

        while(i <= j) {

            while(i <= j && compare(leftIt.next(), pivot) < 0){
                i++;
            }

            while(i <= j && compare(rightIt.previous(), pivot) > 0){
                j--;
            }

            if(i <= j) {
                swap(list, i, j);
                i++;
                j--;
            }
        }
        // Swap pivot to its final place
        swap(list, i, end);
        return i;
    }
}



