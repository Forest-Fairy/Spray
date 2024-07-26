package top.spray.core.dynamic.listener;

import top.spray.core.dynamic.loader.SprayClassLoader;

public interface SprayClassLoaderListener {
    void onClassDefined(SprayClassLoader classLoader, Class<?> clazz);
    void onClassLoaderClose(SprayClassLoader classLoader);
}
