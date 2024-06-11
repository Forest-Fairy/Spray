package top.spray.core.engine.result;

public enum SprayStepResult {

    /** stop for the environment changed, such as the server shut down. no error msg */
    ERROR(-1, "异常停止"),
    /** stop normally. need result message */
    DONE(1, "执行完成"),
    /** stop with exception. need fail message */
    FAIL(0, "执行失败"),
    /** stop by human. need stop reason */
    STOP(2, "手动停止"),
    /** pause by config (debug or collecting data) */
    PAUSED(3, "暂停"),
    ;
    private final int statusCode;
    private final String describeMsg;

    SprayStepResult(int statusCode, String describeMsg) {
        this.statusCode = statusCode;
        this.describeMsg = describeMsg;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getDescribeMsg() {
        return describeMsg;
    }
}
