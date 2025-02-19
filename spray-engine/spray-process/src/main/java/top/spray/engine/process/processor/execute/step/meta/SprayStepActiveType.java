package top.spray.engine.process.processor.execute.step.meta;

import top.spray.core.type.SprayType;
import top.spray.engine.process.processor.execute.i18n.SprayStepExecuteType;

public enum SprayStepActiveType implements SprayStepExecuteType {
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
    public String i18nKey() {
        return this.i18n;
    }

    @Override
    public boolean validInstance(Class<? extends SprayType> clazz) {
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
