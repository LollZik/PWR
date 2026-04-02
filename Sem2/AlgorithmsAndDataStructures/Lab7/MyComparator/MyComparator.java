package MyComparator;

import java.util.Comparator;

public class MyComparator<T extends Comparable<T>> implements Comparator<T> {

    public MyComparator() {
    }

    @Override
    public final int compare(T lhs, T rhs) {
        return lhs.compareTo(rhs);
    }
}
