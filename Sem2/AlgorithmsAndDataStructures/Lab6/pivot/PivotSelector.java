package pivot;

import java.util.List;

public interface PivotSelector<T> {
    int selectPivot(List<T> list, int min, int max);
}