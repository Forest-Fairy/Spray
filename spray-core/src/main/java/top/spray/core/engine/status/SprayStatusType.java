package top.spray.core.engine.status;

import top.spray.core.engine.status.impl.SprayDataDispatchResultStatus;

import java.util.List;
import java.util.Objects;

public interface SprayStatusType {
    int getCode();
    String getDescribeMsg();
    String typeName();
    boolean isSameClass(Class<? extends SprayStatusType> clazz);

    static SprayStatusHolder holder(SprayStatusType sprayStatusType) {
        return SprayStatusHolder.create(sprayStatusType);
    }
    static boolean equal(Object statusType1, Object statusType2) {
        if (statusType1 == null && statusType2 == null) {
            return true;
        }
        if (statusType1 instanceof SprayStatusHolder holder) {
            return holder.equals(statusType2);
        }
        if (statusType2 instanceof SprayStatusHolder holder) {
            return holder.equals(statusType1);
        }
        return Objects.equals(statusType1, statusType2);
    }


    static <T extends SprayStatusType> T get(List<T> values, int code) {
        for (T value : values) {
            if (value.getCode() == code) {
                return value;
            }
        }
        return null;
    }
    static <T extends SprayStatusType> T get(List<T> values, String typeName) {
        for (T value : values) {
            if (value.typeName().equals(typeName)) {
                return value;
            }
        }
        return null;
    }
}
