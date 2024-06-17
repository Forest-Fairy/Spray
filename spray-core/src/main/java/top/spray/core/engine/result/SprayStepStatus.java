package top.spray.core.engine.result;

public enum SprayStepStatus implements SprayStatusType {
    /** pause by config (debug or collecting data) */
    PAUSED(3, "暂停"),
    /** stop by human. need stop reason */
    STOP(2, "手动停止"),
    /** stop normally. need result message */
    DONE(1, "执行完成"),
    /** running by default */
    RUNNING(0, "执行中"),
    /** stop with exception. need fail message */
    FAILED(-1, "执行失败"),
    /** stop for the accident occur, such as the server shut down. no error msg */
    ERROR(-2, "异常停止"),
    ;
    private final int statusCode;
    private final String describeMsg;

    SprayStepStatus(int statusCode, String describeMsg) {
        this.statusCode = statusCode;
        this.describeMsg = describeMsg;
    }

    @Override
    public int getCode() {
        return this.statusCode;
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
        if (statusType instanceof SprayStatusHolder || statusType instanceof SprayStepStatus) {
            return statusType.equals(this);
        } else {
            return false;
        }
    }
}
