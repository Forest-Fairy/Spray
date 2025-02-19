package top.spray.common.cache;

import java.util.function.Function;
import java.util.function.Supplier;

public interface SprayCache {
    /* return null if no exist cache */
    Object setCache(SprayCacheNotifier notifier, String namespace, String key, Object value, Object... params);

    Object getCache(String namespace, String key);

    <T> T compute(Supplier<SprayCacheNotifier> supplierWhenNewOne, String namespace, String key, Function<Object, T> computeFunc, Object... params);

    <T> T computeIfAbsent(Supplier<SprayCacheNotifier> supplierWhenNewOne, String namespace, String key, Supplier<T> supplier, Object... params);

    Object removeCache(String namespace, String key);
}
