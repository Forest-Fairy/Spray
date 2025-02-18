package top.spray.core.dynamic.listener;

import top.spray.core.dynamic.SprayClassLoader;

public interface SprayClassLoaderListener {
    default void newClassDefined(SprayClassLoader classLoader, Class<?> clazz) {}
    default void onClassLoaderClose(SprayClassLoader classLoader) {}
}
