package top.spray.common.bean;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class SprayClassUtil {
    public static final SprayClassUtil INSTANCE = new SprayClassUtil();
    private SprayClassUtil() {}

    public static Class<?> getClassGenericType(Class<?> clazz, int index) {
        Type genericSuperclass = clazz.getGenericSuperclass();
        if (genericSuperclass instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) genericSuperclass;
            Type[] typeArguments = parameterizedType.getActualTypeArguments();
            if (typeArguments.length > 0 && index < typeArguments.length) {
                return (Class<?>) typeArguments[index];
            }
        }
        return null;
    }

    public static Class<?> getClassObject(ClassLoader classLoader, String className, boolean tryWithThreadClassLoader) {
        try {
            return classLoader.loadClass(className);
        } catch (ClassNotFoundException e) {
            ClassNotFoundException exception = e;
            if (tryWithThreadClassLoader && !Thread.currentThread().getContextClassLoader().equals(classLoader)) {
                try {
                    return Thread.currentThread().getContextClassLoader().loadClass(className);
                } catch (ClassNotFoundException ex) {
                    exception = ex;
                }
            }
            throw new IllegalStateException("class " + className + " can not be found", exception);
        }
    }

    public static <T> T newInstance(Class<T> clazz, Class<?>[] parameterTypes, Object... parameters) {
        try {
            Constructor<T> constructor = clazz.getConstructor(parameterTypes);
            constructor.setAccessible(true);
            return constructor.newInstance(parameters);
        } catch (InstantiationException | IllegalAccessException e) {
            throw new IllegalStateException("class " + clazz.getName() + " can not be instantiated", e);
        } catch (InvocationTargetException e) {
            throw new IllegalStateException("failed to instantiate", e);
        } catch (NoSuchMethodException e) {
            throw new IllegalStateException("not suitable constructor found", e);
        }
    }
}
