package top.spray.core.system.dynamic.listener;

import top.spray.core.system.dynamic.SprayClassLoader;

public interface SprayClassLoaderListener {
    default void newClassDefined(SprayClassLoader classLoader, Class<?> clazz) {}
    default void onClassLoaderClose(SprayClassLoader classLoader) {}
}
