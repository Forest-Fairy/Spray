package top.spray.engine.process.processor.execute.step.meta;

import top.spray.engine.process.processor.execute.i18n.SprayStepExecuteType;

public enum SprayExecutorType implements SprayStepExecuteType {
    SOURCE(0, "type.source"),
    COMPUTE(1, "type.compute"),
    TARGET(2, "type.target"),
    ;
    private final int typeCode;
    private final String i18nKey;
    SprayExecutorType(int typeCode, String i18nKey) {
        this.typeCode = typeCode;
        this.i18nKey = i18nKey;
    }

    @Override
    public String i18nKey() {
        return i18nKey;
    }

    @Override
    public int getCode() {
        return typeCode;
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
