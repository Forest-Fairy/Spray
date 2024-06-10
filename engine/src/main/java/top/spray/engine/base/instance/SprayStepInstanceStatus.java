package top.spray.engine.base.instance;

public enum SprayStepInstanceStatus {
    RUNNING("step is running"),
    PAUSED("step is paused"),
    STOP("step is stop"),
    DONE("step is done"),
    ERROR("step is error"),
    ;
    private final String describeMsg;

    SprayStepInstanceStatus(String describeMsg) {
        this.describeMsg = describeMsg;
    }

    public String getDescribeMsg() {
        return describeMsg;
    }
}
