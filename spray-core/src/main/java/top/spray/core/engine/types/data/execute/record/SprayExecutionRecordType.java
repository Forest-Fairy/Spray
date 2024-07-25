package top.spray.core.engine.types.data.execute.record;

import top.spray.core.engine.types.SprayType;
import top.spray.core.engine.types.data.execute.SprayExecutionDescription_i18n;
import top.spray.core.engine.types.data.execute.SprayExecutionTypeName_i18n;
import top.spray.core.i18n.Spray_i18n;

public class SprayExecutionRecordType implements SprayType {
    public static final SprayExecutionRecordType BEFORE_EXECUTE = new SprayExecutionRecordType(0, "execute.record.before");
    public static final SprayExecutionRecordType EXECUTE_SUCCESS = new SprayExecutionRecordType(1, "execute.record.success");
    public static final SprayExecutionRecordType EXECUTE_FAILED = new SprayExecutionRecordType(-1, "execute.record.failed");


    private final int code;
    private final String i18n;

    SprayExecutionRecordType(int code, String i18n) {
        this.code = code;
        this.i18n = i18n;
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String typeName() {
        return Spray_i18n.get(
                SprayExecutionTypeName_i18n.class, this.i18n);
    }

    @Override
    public String getDescribeMsg() {
        return Spray_i18n.get(
                SprayExecutionDescription_i18n.class, this.i18n);
    }

    @Override
    public boolean isSameClass(Class<? extends SprayType> clazz) {
        return SprayExecutionRecordType.class.isAssignableFrom(clazz);
    }

    @Override
    public boolean equals(Object obj) {
        return SprayType.equal(this, obj);
    }
}
