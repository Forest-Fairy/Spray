package top.spray.core.engine.status.impl;

import top.spray.core.engine.status.SprayStatusType;

public class SprayCoordinateStatus implements SprayStatusType {
    /** stop by human. need stop reason */
    public static final SprayCoordinateStatus STOP = new SprayCoordinateStatus("STOP", 2, "手动停止");

    /** stop normally. need status message */
    public static final SprayCoordinateStatus SUCCESS = new SprayCoordinateStatus("SUCCESS", 1, "执行完成");
    /** running by default */
    public static final SprayCoordinateStatus RUNNING = new SprayCoordinateStatus("RUNNING", 0, "执行中");
    /** stop with exception. need fail message */
    public static final SprayCoordinateStatus FAILED = new SprayCoordinateStatus("FAILED", -1, "执行失败");
    /** stop for the accident occur, such as the server shut down. no error msg */
    public static final SprayCoordinateStatus ERROR = new SprayCoordinateStatus("ERROR", -2, "异常停止");

    private final String typeName;
    private final int code;
    private final String describeMsg;
    SprayCoordinateStatus(String typeName, int code, String describeMsg) {
        this.typeName = typeName;
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
        return this.typeName;
    }

    @Override
    public boolean isSameClass(Class<? extends SprayStatusType> clazz) {
        return SprayCoordinateStatus.class.isAssignableFrom(clazz);
    }

    @Override
    public boolean equals(Object obj) {
        return SprayStatusType.equal(this, obj);
    }
}
