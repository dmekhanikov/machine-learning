package megabyte.ml.util;

import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

public class Lists {

    public static <T> int partition(List<T> list, Predicate<T> predicate) {
        int i = 0;
        for (int j = 0; j < list.size(); j++) {
            if (predicate.test(list.get(j))) {
                Collections.swap(list, i, j);
                i++;
            }
        }
        return i;
    }
}
