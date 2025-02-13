package top.spray.common.bean;

import org.apache.commons.lang3.reflect.MethodUtils;

import java.lang.reflect.Method;

public class SprayMethodUtil extends MethodUtils {
    public static final SprayMethodUtil INSTANCE = new SprayMethodUtil();
    private SprayMethodUtil() {}
    public static Integer size(Object o) {
        Method sizeMethod = MethodUtils.getMatchingMethod(
                o.getClass(), "size");
        if (sizeMethod == null) {
            return null;
        }
        try {
            if (!sizeMethod.canAccess(o)) {
                sizeMethod.setAccessible(true);
            }
            return (Integer) sizeMethod.invoke(o);
        } catch (Exception e) {
            return null;
        }
    }
    public static Integer length(Object o) {
        Method lengthMethod = MethodUtils.getMatchingMethod(
                o.getClass(), "length");
        if (lengthMethod == null) {
            return null;
        }
        try {
            if (!lengthMethod.canAccess(o)) {
                lengthMethod.setAccessible(true);
            }
            return (Integer) lengthMethod.invoke(o);
        } catch (Exception e) {
            return null;
        }
    }
    public static Boolean isEmpty(Object o) {
        Method isEmptyMethod = MethodUtils.getMatchingMethod(
                o.getClass(), "isEmpty");
        if (isEmptyMethod == null) {
            return null;
        }
        try {
            if (!isEmptyMethod.canAccess(o)) {
                isEmptyMethod.setAccessible(true);
            }
            return (Boolean) isEmptyMethod.invoke(o);
        } catch (Exception e) {
            return null;
        }
    }

}
