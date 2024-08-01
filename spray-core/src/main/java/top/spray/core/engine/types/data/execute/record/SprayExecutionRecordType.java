package top.spray.core.engine.types.data.execute.record;

import top.spray.core.engine.types.SprayType;
import top.spray.core.engine.types.data.execute.SprayExecutionDescription_i18n;
import top.spray.core.engine.types.data.execute.SprayExecutionTypeName_i18n;
import top.spray.core.i18n.Spray_i18n;

import java.util.List;

public class SprayExecutionRecordType implements SprayType {
    private static final List<SprayExecutionRecordType> values = List.of(
            SprayExecutionRecordType.RECORD_BEFORE_EXECUTE,
            SprayExecutionRecordType.RECORD_EXECUTE_SUCCESS,
            SprayExecutionRecordType.RECORD_EXECUTE_FAILED
    );
    public static List<SprayExecutionRecordType> values() {
        return values;
    }
    public static SprayExecutionRecordType get(int code) {
        return SprayType.get(values, code);
    }


    public static final SprayExecutionRecordType RECORD_BEFORE_EXECUTE = new SprayExecutionRecordType(0, "record.type.before");
    public static final SprayExecutionRecordType RECORD_EXECUTE_SUCCESS = new SprayExecutionRecordType(1, "record.type.success");
    public static final SprayExecutionRecordType RECORD_EXECUTE_FAILED = new SprayExecutionRecordType(-1, "record.type.failed");


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
        return SprayType.isEqual(this, obj);
    }
}
