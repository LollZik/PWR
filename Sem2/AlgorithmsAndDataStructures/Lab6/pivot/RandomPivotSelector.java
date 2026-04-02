package pivot;
import java.util.List;
import java.util.Random;


public class RandomPivotSelector<T> implements PivotSelector<T> {

    @Override
    public int selectPivot(List<T> list, int min, int max) {
        Random rand = new Random();
        return (rand.nextInt(max - min + 1) + min);
    }
}