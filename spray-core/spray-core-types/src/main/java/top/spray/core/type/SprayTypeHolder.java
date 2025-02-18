package top.spray.core.type;


import cn.hutool.core.lang.Assert;

public class SprayTypeHolder<T extends SprayType> implements SprayType {
    public static <T extends SprayType> SprayTypeHolder<T> create(T sprayType) {
        return new SprayTypeHolder<>(sprayType);
    }
    private final T actualType;
    private int code;
    private String describeMsg;
    private String typeName;
    private SprayTypeHolder(T statusType) {
        Assert.notNull(statusType, "default status type should not be null!");
        this.actualType = statusType;
        this.set(statusType);
    }
    public void set(SprayType type) {
        if (! actualType.isSameClass(type.getClass())) {
            throw new IllegalArgumentException("can not set status with the different type");
        }
        this.code = type.getCode();
        this.describeMsg = type.getDescription();
        this.typeName = type.typeName();
    }

    @Override
    public int getCode() {
        return this.code;
    }

    @Override
    public String getDescription() {
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
        if (obj instanceof SprayTypeHolder<?> holder) {
            return SprayType.isEqual(this.actualType, holder.actualType);
        } else if (obj instanceof SprayType type) {
            return SprayType.isEqual(this.actualType, type);
        } else {
            return false;
        }
    }
}
