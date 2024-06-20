package top.spray.core.engine.execute;

public enum SprayExecutorType {
    SOURCE(0),
    COMPUTE(1),
    TARGET(2);
    ;
    private int typeCode;
    SprayExecutorType(int typeCode) {
        this.typeCode = typeCode;
    }

    public int typeCode() {
        return typeCode;
    }
    public String type() {
        return this.name();
    }

    public static SprayExecutorType typeOf(int typeCode) {
        for (SprayExecutorType value : SprayExecutorType.values()) {
            if (value.typeCode == typeCode) {
                return value;
            }
        }
        throw new IllegalArgumentException("typeCode is not a valid SprayExecutorTypeCode");
    }

    public static SprayExecutorType typeOf(String typeName) {
        try {
            return SprayExecutorType.valueOf(typeName.toUpperCase());
        } catch (Exception e) {
            throw new IllegalArgumentException("typeName is not a valid SprayExecutorType", e);
        }
    }
}
