package megabyte.ml.util;

import lombok.AllArgsConstructor;

import java.util.Comparator;

@AllArgsConstructor
public class Pair<T, F> {

    private T first;
    private F second;

    public T first() {
        return first;
    }

    public F second() {
        return second;
    }

    public static <P extends Comparable<P>, Q> Comparator<Pair<P, Q>> firstComparator() {
        return (p1, p2) -> p1.first.compareTo(p2.first);
    }

    public static <P, Q extends Comparable<Q>> Comparator<Pair<P, Q>> secondComparator() {
        return (p1, p2) -> p1.second.compareTo(p2.second);
    }
}
