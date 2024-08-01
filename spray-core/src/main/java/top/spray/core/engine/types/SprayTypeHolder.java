package top.spray.core.engine.types;


import cn.hutool.core.lang.Assert;

public class SprayTypeHolder implements SprayType {
    public static SprayTypeHolder create(SprayType sprayType) {
        return new SprayTypeHolder(sprayType);
    }
    private final SprayType actualType;
    private int code;
    private String describeMsg;
    private String typeName;
    private SprayTypeHolder(SprayType statusType) {
        Assert.notNull(statusType, "default status type should not be null!");
        this.actualType = statusType;
        this.set(statusType);
    }
    public void set(SprayType type) {
        if (actualType.getClass().isInstance(type)) {
            throw new IllegalArgumentException("can not set status with the different type");
        }
        this.code = type.getCode();
        this.describeMsg = type.getDescribeMsg();
        this.typeName = type.typeName();
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
    public boolean isSameClass(Class<? extends SprayType> clazz) {
        return actualType.isSameClass(clazz);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof SprayTypeHolder holder) {
            return SprayType.isEqual(this.actualType, holder.actualType);
        } else if (obj instanceof SprayType type) {
            return SprayType.isEqual(this.actualType, type);
        } else {
            return false;
        }
    }
}
