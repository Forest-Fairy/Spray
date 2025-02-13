package top.spray.common.tools;

import java.util.*;
import java.util.function.Supplier;

public class SpraySynchronizeUtils {
    public static final SpraySynchronizeUtils INSTANCE = new SpraySynchronizeUtils();
    private SpraySynchronizeUtils() {}
    public static <K, V> V synchronizeGet(Map<K, V> map, K key, Supplier<V> newVal) {
        V v = map.get(key);
        if (v == null) {
            /*unchecked*/
            synchronized (map) {
                if ((v = map.get(key)) == null) {
                    map.put(key, v = newVal.get());
                }
            }
        }
        return v;
    }
}
