package top.spray.common.cache;

import java.util.function.Function;
import java.util.function.Supplier;

public interface SprayCache {
    /* return null if no exist cache */
    Object setCache(String namespace, String key, Object value, Object... params);

    Object getCache(String namespace, String key);

    <T> T computeIfAbsent(String namespace, String key, Function<String, T> func, Object... params);

    Object removeCache(String namespace, String key);
}
