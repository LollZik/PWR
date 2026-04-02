import core.AbstractSwappingSortingAlgorithm;
import java.util.Comparator;
import java.util.List;

public class InsertionSort<T> extends AbstractSwappingSortingAlgorithm<T> {

    public InsertionSort(Comparator<? super T> comparator){
        super(comparator);
    }

    public List<T> sort(List<T> list){
        int j;
        for (int i = list.size()-1; i >= 0;i--) {
            T value = list.get(i);
            int insertIndex = binarySearch(list,value,i+1,list.size());
            for (j = i; j < insertIndex-1; j++){
                swap(list, j, (j+1));
            }
        }
        return list;
    }

    private int binarySearch(List<T> list, T value, int left, int right) {
        while (left < right) {
            int mid = (left + right) / 2;
            if (compare(value, list.get(mid)) > 0) {
                left = mid + 1;
            } else {
                right = mid;
            }
        }
        return left;
    }
}

