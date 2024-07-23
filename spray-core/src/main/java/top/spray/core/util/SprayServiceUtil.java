package top.spray.core.util;

import cn.hutool.cache.Cache;
import cn.hutool.cache.CacheUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SprayServiceUtil {
    private SprayServiceUtil() {}


    /* cache expire time is 1 hour */
    private static final Cache<String, Map<String, ?>> CACHE =
            CacheUtil.newTimedCache(1000L * 60L * 60L);
    static {
        CACHE.setListener((k, v) -> v.clear());
    }
    public static <T> Map<String, T> loadServiceClassNameMapCache(Class<T> serviceClass) {
        return loadServiceClassNameMapCache(serviceClass, serviceClass.getName(), null);
    }
    public static <T> Map<String, T> loadServiceClassNameMapCache(Class<T> serviceClass, String cacheKey, Predicate<T> p) {
        Map<String, ?> cacheObj = CACHE.get(cacheKey);
        if (cacheObj == null) {
            synchronized (CACHE) {
                if ((cacheObj = CACHE.get(cacheKey)) == null) {
                    CACHE.put(cacheKey, cacheObj = loadServiceClassNameMap(serviceClass, p));
                }
            }
        }
        return (Map<String, T>) cacheObj;
    }

    public static <T> Map<String, T> loadServiceClassNameMap(Class<T> serviceClass) {
        return loadServiceClassNameMap(serviceClass, null);
    }
    private static <T> Map<String, T> loadServiceClassNameMap(Class<T> serviceClass, Predicate<T> p) {
        if (!serviceClass.isInterface()) {
            throw new IllegalArgumentException("only interface class is supported");
        }
        return ServiceLoader.load(serviceClass).stream()
                .map(ServiceLoader.Provider::get)
                .filter(s -> p == null || p.test(s))
                .collect(Collectors.toMap(t -> t.getClass().getName(), t -> t));
    }







}
