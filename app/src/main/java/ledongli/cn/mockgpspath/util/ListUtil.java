package ledongli.cn.mockgpspath.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiangying on 8/15/15.
 * desc:
 */
public class ListUtil {
    private static final String TAG = "ListUtil";

    public interface Transferable<E,T> {
        E transfer(T t);
    }

    public static <E,T> List<E> trasList(List<T> list, Transferable<E,T> transfer) {
        if (null == list || list.size() <= 0) {
            return null;
        }
        List<E> result = new ArrayList<E>(list.size());
        for (T obj : list) {
            result.add(transfer.transfer(obj));
        }

        return result;
    }
}
