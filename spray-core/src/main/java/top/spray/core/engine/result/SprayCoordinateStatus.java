package top.spray.core.engine.result;

public enum SprayCoordinateStatus implements SprayStatusType {
    /** stop by human. need stop reason */
    STOP(2, "手动停止"),
    /** stop normally. need result message */
    SUCCESS(1, "执行完成"),
    /** running by default */
    RUNNING(0, "执行中"),
    /** stop with exception. need fail message */
    FAILED(-1, "执行失败"),
    /** stop for the accident occur, such as the server shut down. no error msg */
    ERROR(-2, "异常停止"),
    ;
    private final int code;
    private final String describeMsg;
    SprayCoordinateStatus(int code, String describeMsg) {
        this.code = code;
        this.describeMsg = describeMsg;
    }

    @Override
    public int getCode() {
        return this.code;
    }

    @Override
    public String getDescribeMsg() {
        return this.describeMsg;
    }

    @Override
    public String typeName() {
        return this.name();
    }

    @Override
    public boolean match(SprayStatusType statusType) {
        if (statusType instanceof SprayStatusHolder || statusType instanceof SprayCoordinateStatus) {
            return statusType.equals(this);
        } else {
            return false;
        }
    }
}
