package pivot;
import java.util.List;


public class FirstElementPivotSelector<T> implements PivotSelector<T> {

    @Override
    public int selectPivot(List<T> list, int min, int max) {
        return (min);
    }
}