package top.spray.core.engine.types;


import java.util.List;

public interface SprayType {
    int getCode();
    String typeName();
    String getDescribeMsg();
    boolean isSameClass(Class<? extends SprayType> clazz);

    static SprayTypeHolder holder(SprayType sprayStatusType) {
        return SprayTypeHolder.create(sprayStatusType);
    }
    static boolean isEqual(Object t1, Object t2) {
        if (t1 == null && t2 == null) {
            return true;
        }
        if (t1 instanceof SprayTypeHolder holder) {
            return holder.equals(t2);
        }
        if (t2 instanceof SprayTypeHolder holder) {
            return holder.equals(t1);
        }
        if (t1 instanceof SprayType type1 && t2 instanceof SprayType type2) {
            if (t1.getClass().isAssignableFrom(t2.getClass()) || t2.getClass().isAssignableFrom(t1.getClass())) {
                // 同一个类或是父子类才可以
                return type1.getCode() == type2.getCode();
            }
        }
        return false;
    }


    static <T extends SprayType> T get(List<T> values, int code) {
        for (T value : values) {
            if (value.getCode() == code) {
                return value;
            }
        }
        return null;
    }
    static <T extends SprayType> T get(List<T> values, String typeName) {
        for (T value : values) {
            if (value.typeName().equals(typeName)) {
                return value;
            }
        }
        return null;
    }
}
