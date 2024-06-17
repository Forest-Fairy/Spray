package top.spray.core.engine.result;

public interface SprayStatusType {
    int getCode();
    String getDescribeMsg();
    String typeName();
    boolean match(SprayStatusType statusType);
    static SprayStatusHolder create(SprayStatusType sprayStatusType) {
        return new SprayStatusHolder(sprayStatusType);
    }
}
