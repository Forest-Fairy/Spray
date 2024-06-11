package top.spray.core.engine.result;

public enum SprayCoordinateStatus {
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
    private final String msg;
    SprayCoordinateStatus(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
    public int getCode() {
        return code;
    }
    public String getMsg() {
        return msg;
    }
}
