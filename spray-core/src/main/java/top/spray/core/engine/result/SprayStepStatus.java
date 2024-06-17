package top.spray.core.engine.result;

public class SprayStepStatus implements SprayStatusType {
    /** pause by config (debug or collecting data) */
    public static final SprayStepStatus PAUSED = new SprayStepStatus("PAUSED", 3, "暂停");
    /** stop by human. need stop reason */
    public static final SprayStepStatus STOP = new SprayStepStatus("STOP", 2, "手动停止");
    /** stop normally. need result message */
    public static final SprayStepStatus DONE = new SprayStepStatus("DONE", 1, "执行完成");
    /** running by default */
    public static final SprayStepStatus RUNNING = new SprayStepStatus("RUNNING", 0, "执行中");
    /** stop with exception. need fail message */
    public static final SprayStepStatus FAILED = new SprayStepStatus("FAILED", -1, "执行失败");
    /** stop for the accident occur, such as the server shut down. no error msg */
    public static final SprayStepStatus ERROR = new SprayStepStatus("ERROR", -2, "异常停止");

    private final String typeName;
    private final int statusCode;
    private final String describeMsg;

    SprayStepStatus(String typeName, int statusCode, String describeMsg) {
        this.typeName = typeName;
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
        return this.typeName;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof SprayStatusHolder holder) {
            return holder.equals(this);
        } else if (obj instanceof SprayStepStatus) {
            return super.equals(obj);
        } else {
            return false;
        }
    }
}
