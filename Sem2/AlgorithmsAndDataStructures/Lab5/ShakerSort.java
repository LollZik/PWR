import java.util.Comparator;
import java.util.List;

import core.AbstractSwappingSortingAlgorithm;

public class ShakerSort<T> extends AbstractSwappingSortingAlgorithm<T> {

    public ShakerSort(Comparator<? super T> comparator) {
        super(comparator);
    }

    @Override
    public List<T> sort(List<T> list) {
        int leftBound = 0;
        int rightBound = list.size() - 1;
        int lastSwap = rightBound;

        while(leftBound < rightBound) {
            boolean swapped = false;
            for(int left = 0; left < rightBound; ++left) {
                int right = left + 1;

                if(compare(list.get(left), list.get(right)) > 0) {
                    swap(list, left, right);
                    lastSwap = left;
                    swapped = true;
                }
            }
            rightBound = lastSwap;

            for(int right = rightBound; right > leftBound; right--){
                int left = right - 1;

                if(compare(list.get(right), list.get(left)) < 0){
                    swap(list,right, left);
                    swapped = true;
                    lastSwap = right;
                }
            }
            if(!swapped){
                break;
            }
            leftBound = lastSwap;
        }
        return list;
    }
}
