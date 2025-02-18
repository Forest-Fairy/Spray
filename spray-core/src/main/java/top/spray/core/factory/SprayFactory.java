package top.spray.core.factory;

import top.spray.common.bean.SprayServiceUtil;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public interface SprayFactory<Product> {
    default String factoryNameSpace() {
        return SprayFactory.class.getName();
    }
    boolean support(SprayMaterial material);
    Product produce(SprayMaterial material);

    class Container {
        private static final Map<String, Map<String, SprayFactory<?>>> CONTAINER = new ConcurrentHashMap<>();
        private static void register(Class<? extends SprayFactory<?>> factoryClass, SprayFactory<?> factory) {
            CONTAINER.computeIfAbsent(factoryClass.getName(),
                    namespace ->
                            new ConcurrentHashMap<>(SprayServiceUtil.loadServiceClassNameMap(factoryClass)))
                    .put(factory.factoryNameSpace(), factory);
        }
        private static <T extends SprayFactory<?>> T get(Class<T> factoryClass, String name) {
            return (T) CONTAINER.get(factoryClass.getName()).get(name);
        }
        private static <T extends SprayFactory<?>> Map<String, T> getAll(Class<T> factoryClass) {
            Map<String, SprayFactory<?>> factoryMap = CONTAINER.get(factoryClass.getName());
            if (factoryMap == null) {
                // register by spi
                synchronized (CONTAINER) {
                    if ((factoryMap = CONTAINER.get(factoryClass.getName())) == null) {
                        factoryMap = new ConcurrentHashMap<>();
                        Map<String, T> factories = SprayServiceUtil.loadServiceClassNameMap(factoryClass);
                        for (T f : factories.values()) {
                            factoryMap.put(f.factoryNameSpace(), f);
                        }
                        if (!factoryMap.isEmpty()) {
                            CONTAINER.put(factoryClass.getName(), factoryMap);
                        }
                    }
                }
            }
            return (Map<String, T>) factoryMap;
        }

        public static void registerFactoryBean(Class<? extends SprayFactory<?>> factoryClass, SprayFactory<?> factory) {
            register(factoryClass, factory);
        }
        public static <T extends SprayFactory<?>> Map<String, T> getRegisteredFactories(Class<T> factoryClass) {
            return getAll(factoryClass);
        }
    }
}
