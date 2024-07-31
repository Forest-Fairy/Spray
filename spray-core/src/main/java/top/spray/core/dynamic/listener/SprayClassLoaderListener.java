package top.spray.core.dynamic.listener;

import top.spray.core.dynamic.loader.SprayClassLoader;

public interface SprayClassLoaderListener {
    default void onClassDefined(SprayClassLoader classLoader, Class<?> clazz) {}
    default void onClassLoaderClose(SprayClassLoader classLoader) {}
}
