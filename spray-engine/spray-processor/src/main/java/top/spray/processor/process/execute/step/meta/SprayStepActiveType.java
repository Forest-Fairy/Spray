package top.spray.processor.process.execute.step.meta;

import top.spray.core.i18n.Spray_i18nBundleDef;
import top.spray.core.system.type.SprayType;
import top.spray.core.system.type.SprayTypeI18nType;
import top.spray.processor.process.execute.i18n.SprayExecuteTypeName;
import top.spray.processor.process.execute.i18n.SprayExecuteDescription;

public enum SprayStepActiveType implements SprayTypeI18nType {
    /** this step will be executed */
    ACTIVE(1, "active_type.active"),
    /** this step won't be executed but its next nodes may be */
    SKIP(0, "active_type.skip"),
    /** this step and its next nodes won't be executed  */
    BREAK(-1, "active_type.break"),
    ;
    private final int code;
    private final String i18n;

    SprayStepActiveType(int code, String i18n) {
        this.code = code;
        this.i18n = i18n;
    }

    @Override
    public int getCode() {
        return this.code;
    }

    @Override
    public Class<? extends Spray_i18nBundleDef> i18nClass(TargetType type) {
        if (type == TargetType.NAME) {
            return SprayExecuteTypeName.class;
        } else if (type == TargetType.DESCRIPTION) {
            return SprayExecuteDescription.class;
        }
        return unsupported(type);
    }


    @Override
    public String i18nKey() {
        return this.i18n;
    }

    @Override
    public boolean isSameClass(Class<? extends SprayType> clazz) {
        return SprayStepActiveType.class.equals(clazz);
    }

    public static SprayStepActiveType of(int code) {
        for (SprayStepActiveType value : values()) {
            if (value.getCode() == code) {
                return value;
            }
        }
        return null;
    }
}
