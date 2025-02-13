package top.spray.common.bean;

import org.apache.commons.lang3.reflect.FieldUtils;
import top.spray.common.tools.SprayTester;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.function.Consumer;

public class SprayFieldUtil extends FieldUtils {
    public static final SprayFieldUtil INSTANCE = new SprayFieldUtil();
    private SprayFieldUtil() {}

    public static Integer getLength(Object o) {
        Field lengthField = FieldUtils.getField(o.getClass(), "length");
        return SprayTester.supply(
                () -> {
                    if (!lengthField.canAccess(o)) {
                        lengthField.setAccessible(true);
                    }
                    return (Integer) lengthField.get(o);
                }, null);
        // simplify 'throwable -> null' to 'null' if null is default value
//        return SprayTester.test(
//                () -> {
//                    if (!lengthField.canAccess(o)) {
//                        lengthField.setAccessible(true);
//                    }
//                    return (Integer) lengthField.get(o);
//                }, throwable -> null);

        // simplify by tester
//        if (lengthField == null) {
//            return null;
//        }
//        try {
//            if (!lengthField.canAccess(o)) {
//                lengthField.setAccessible(true);
//            }
//            return (Integer) lengthField.get(o);
//        } catch (Exception e) {
//            return null;
//        }
    }
    public static Integer getSize(Object o) {
        Field sizeField = FieldUtils.getField(o.getClass(), "size");
        return SprayTester.supply(() -> {
            if (!sizeField.canAccess(o)) {
                sizeField.setAccessible(true);
            }
            return (Integer) sizeField.get(o);
        }, null);
    }


    public static Object getFieldValue(Object bean, String fieldName) {
        return bean == null ? null :
                getFieldValue(bean, getField(bean.getClass(), fieldName));
    }

    public static Object getFieldValue(Object bean, Field field) {
        return bean == null ? null :
                SprayTester.supply(() -> {
                    if (! field.canAccess(bean)) {
                        field.setAccessible(true);
                    }
                    return field.get(bean);
                }, null);
    }

    public static Field getField(Class<?> clazz, Class<?> fieldType) {
        return getAllFieldsList(clazz).stream()
                .filter(field -> fieldType.isAssignableFrom(field.getType()))
                .findFirst().orElse(null);
    }

    public static Field getDeclaredField(Class<?> clazz, Class<?> fieldType) {
        return Arrays.stream(clazz.getDeclaredFields())
                .filter(field -> fieldType.isAssignableFrom(field.getType()))
                .findFirst().orElse(null);
    }

    public static boolean setFieldValue(Field field, Object bean, Object val) {
        return setFieldValue(field, bean, val, false, null);
    }
    public static boolean setFieldValue(Field field, Object bean, Object val, boolean forceAccess, Consumer<Throwable> errorHandler) {
        if (! field.canAccess(bean)) {
            if (forceAccess) {
                field.setAccessible(true);
            } else {
                return false;
            }
        }
        try {
            field.set(bean, val);
        } catch (Throwable t) {
            if (errorHandler != null) {
                errorHandler.accept(t);
            }
            return false;
        }
        return true;
    }
}
