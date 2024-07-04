package top.spray.core.engine.result;


import cn.hutool.core.lang.Assert;

import java.util.Objects;

public class SprayStatusHolder implements SprayStatusType {
    private int code;
    private String describeMsg;
    private String typeName;

    private SprayStatusType actualStatusType;

    public SprayStatusHolder(SprayStatusType statusType) {
        Assert.notNull(statusType, "default status type should not be null!");
        this.actualStatusType = statusType;
        this.setStatus(statusType);
    }
    public void setStatus(SprayStatusType statusType) {
        this.code = statusType.getCode();
        this.describeMsg = statusType.getDescribeMsg();
        this.typeName = statusType.typeName();
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
        if (obj instanceof SprayStatusHolder holder) {
            return Objects.equals(this.actualStatusType, holder.actualStatusType);
        } else {
            return Objects.equals(this.actualStatusType, obj);
        }
    }
}
