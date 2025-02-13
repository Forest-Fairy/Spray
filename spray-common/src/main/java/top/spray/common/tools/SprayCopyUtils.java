package top.spray.common.tools;

import top.spray.common.data.SprayStringUtils;

import java.lang.reflect.Array;
import java.util.Arrays;

public class SprayCopyUtils {
    public static final SprayCopyUtils INSTANCE = new SprayCopyUtils();
    private SprayCopyUtils() {}

    public static <T> T[] arrayCopy(T[] src, int form, int to) {
        return arrayCopy(src, form, to, (Class<? extends T[]>) src.getClass());
    }

    public static <T,U> T[] arrayCopy(U[] original, int from, int to, Class<? extends T[]> newType) {
        int newLength = to - from;
        if (newLength < 0)
            throw new IllegalArgumentException(from + " > " + to);
        @SuppressWarnings("unchecked")
        T[] copy = (newType.componentType() == Object.class)
                ? (T[]) new Object[newLength]
                : (T[]) Array.newInstance(newType.getComponentType(), newLength);
        System.arraycopy(original, from, copy, 0,
                Math.min(original.length - from, newLength));
        return copy;
//        return Arrays.copyOfRange(original, from, to, newType);
    }
    public static <T> void arrayCopy(T[] src, int fromSrc, int toSrc, T[] dest, int fromDest) {
        System.arraycopy(src, fromSrc, dest, fromDest,
                Math.min(toSrc - fromSrc, dest.length - fromDest));
    }

}
