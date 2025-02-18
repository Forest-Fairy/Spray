package top.spray.processor.process.data.event.impl;

import top.spray.core.system.type.SprayType;
import top.spray.core.system.type.SprayTypeI18nType;
import top.spray.core.i18n.SprayResourceBundleDef;
import top.spray.processor.process.execute.i18n.SprayExecuteDescription;
import top.spray.processor.process.execute.i18n.SprayExecuteTypeName;

import java.util.List;

public class SprayDataExecuteEventType implements SprayTypeI18nType {
    private static final List<SprayDataExecuteEventType> values = List.of(
            SprayDataExecuteEventType.RECORD_BEFORE_EXECUTE,
            SprayDataExecuteEventType.RECORD_EXECUTE_SUCCESS,
            SprayDataExecuteEventType.RECORD_EXECUTE_FAILED
    );
    public static List<SprayDataExecuteEventType> values() {
        return values;
    }
    public static SprayDataExecuteEventType get(int code) {
        return SprayType.get(values, code);
    }

    public static final SprayDataExecuteEventType RECORD_BEFORE_EXECUTE = new SprayDataExecuteEventType(0, "record.type.before");
    public static final SprayDataExecuteEventType RECORD_EXECUTE_SUCCESS = new SprayDataExecuteEventType(1, "record.type.success");
    public static final SprayDataExecuteEventType RECORD_EXECUTE_FAILED = new SprayDataExecuteEventType(-1, "record.type.failed");

    private final int code;
    private final String i18n;

    SprayDataExecuteEventType(int code, String i18n) {
        this.code = code;
        this.i18n = i18n;
    }

    @Override
    public int getCode() {
        return code;
    }


    @Override
    public boolean equals(Object obj) {
        return SprayType.isEqual(this, obj);
    }

    @Override
    public String i18nKey() {
        return this.i18n;
    }

    @Override
    public Class<? extends SprayResourceBundleDef> i18nClass(TargetType target) {
        return switch (target) {
            case NAME -> SprayExecuteTypeName.class;
            case DESCRIPTION -> SprayExecuteDescription.class;
            default -> unsupported(target);
        };
    }
}
