package top.spray.core.engine.result.impl;

import top.spray.core.engine.result.SprayStatusType;

public class SprayDataDispatchResultStatus implements SprayStatusType {

    /** successfully execute with step */
    public static final SprayDataDispatchResultStatus SUCCESS = new SprayDataDispatchResultStatus("SUCCESS", 1, "执行完成");
    /** the data is abandoned because there is no next step for executing */
    public static final SprayDataDispatchResultStatus ABANDONED = new SprayDataDispatchResultStatus("ABANDONED", 0, "丢弃");
    /** skipped by node config */
    public static final SprayDataDispatchResultStatus SKIPPED = new SprayDataDispatchResultStatus("SKIPPED", -1, "跳过");
    /** filtered by filter */
    public static final SprayDataDispatchResultStatus FILTERED = new SprayDataDispatchResultStatus("FILTERED", -2, "被过滤");
    /** exception occur. need fail message */
    public static final SprayDataDispatchResultStatus FAILED = new SprayDataDispatchResultStatus("FAILED", -3, "执行失败");
    /** the transaction stop but data status is not SUCCESS or FAILED */
    public static final SprayDataDispatchResultStatus ERROR = new SprayDataDispatchResultStatus("ERROR", -4, "异常停止");

    private final String typeName;
    private final int code;
    private final String describeMsg;
    SprayDataDispatchResultStatus(String typeName, int code, String describeMsg) {
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
    public boolean equals(Object obj) {
        return SprayStatusType.equal(this, obj);
    }
}
