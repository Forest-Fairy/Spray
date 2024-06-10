package top.spray.engine.base.instance;

public enum SprayInstanceStatus {
    RUNNING("engine is running"),
    PAUSED("engine is paused"),
    STOP("engine is stop"),
    DONE("engine is stop"),
    ERROR("engine is error"),
    ;
    private final String describeMsg;

    SprayInstanceStatus(String describeMsg) {
        this.describeMsg = describeMsg;
    }

    public String getDescribeMsg() {
        return describeMsg;
    }
}
