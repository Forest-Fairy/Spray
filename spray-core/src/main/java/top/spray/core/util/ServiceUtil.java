package top.spray.core.util;

import cn.hutool.cache.Cache;
import cn.hutool.cache.CacheUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.stream.Collectors;

public class ServiceUtil {
    private static Cache<String, Map<String, ?>> CACHE =
            CacheUtil.newTimedCache(1000L * 60L * 60L);
    static {
        CACHE.setListener((k, v) -> v.clear());
    }
    public static <T> Map<String, T> loadServiceClassNameMapCache(Class<T> serviceClass) {
        Map<String, ?> cacheObj = CACHE.get(serviceClass.getName());
        if (cacheObj == null) {
            synchronized (CACHE) {
                if ((cacheObj = CACHE.get(serviceClass.getName())) == null) {
                    CACHE.put(serviceClass.getName(), cacheObj = loadServiceClassNameMap(serviceClass));
                }
            }
        }
        return (Map<String, T>) cacheObj;
    }
    public static <T> Map<String, T> loadServiceClassNameMap(Class<T> serviceClass) {
        if (!serviceClass.isInterface()) {
            throw new IllegalArgumentException("only interface class is supported");
        }
        ServiceLoader<T> load = ServiceLoader.load(serviceClass);
        return load.findFirst().isPresent() ? load.stream()
                .map(ServiceLoader.Provider::get)
                .collect(Collectors.toMap(t -> t.getClass().getName(), t -> t))
                : new HashMap<>();
    }







}
