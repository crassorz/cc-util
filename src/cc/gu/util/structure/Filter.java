package cc.gu.util.structure;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cc on 2016/7/15.
 */
public class Filter {
    public static void remove(List<?> list, ValueIs is) {
        for (int i = list.size() - 1; i >= 0; i--) {
            if (!is.is(list.get(i))) {
                list.remove(i);
            }
        }
    }
    public static <T> void copyTo(List<? extends T> from, List<T> to, ValueIs is) {
        for (T t:from) {
            if (is.is(t)) {
                to.add(t);
            }
        }
    }
    public static <T> List<T> copyTo(List<? extends T> from, ValueIs is) {
        List<T> to = new ArrayList<>();
        copyTo(from, to, is);
        return to;
    }
}
