package top.spray.core.engine.result;

public enum SprayCoordinateResult {
    /** stop for the environment changed, such as the server shut down. no error msg */
    ERROR(-1, "异常停止"),
    /** stop normally. need result message */
    SUCCESS(1, "执行成功"),
    /** stop with exception. need fail message */
    FAIL(0, "执行失败"),
    /** stop by human. need stop reason */
    STOP(2, "手动停止"),
    ;
    private final int code;
    private final String msg;
    SprayCoordinateResult(int code, String msg) {
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
