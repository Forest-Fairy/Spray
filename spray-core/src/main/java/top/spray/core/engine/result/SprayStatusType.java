package top.spray.core.engine.result;

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
}
