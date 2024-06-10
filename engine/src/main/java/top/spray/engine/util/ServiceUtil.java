package top.spray.engine.util;

import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.stream.Collectors;

public class ServiceUtil {
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
