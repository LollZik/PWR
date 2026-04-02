import core.AbstractSwappingSortingAlgorithm;
import java.util.Comparator;
import java.util.List;

public class SelectionSort<T> extends AbstractSwappingSortingAlgorithm<T> {

    public SelectionSort(Comparator<? super T> comparator){
        super(comparator);
    }

    public List<T> sort(List<T> list){
        int maxIndex;
        T maxVal;
        int last = list.size()-1;

        while (last > 0) {
            maxIndex = 0;
            maxVal = list.getFirst();

            for (int i = 1; i <= last; i++) {
                if (compare(maxVal, list.get(i)) < 0) { // i > max
                    maxVal = list.get(i);
                    maxIndex = i;
                }
            }
            swap(list, maxIndex, last);
            last--;
        }
        return list;
    }
}
