package top.spray.core.stream;

import top.spray.core.i18n.SprayResourceBundleDef;
import top.spray.core.global.stream.i18n.SprayStreamStatusComment_i18n;
import top.spray.core.global.stream.i18n.SprayStreamStatusName_i18n;
import top.spray.core.system.type.SprayTypeI18nType;

public enum SprayDataStreamStatus implements SprayTypeI18nType {
    OPEN(0, "stream.open"),
    CLOSE(0, "stream.close"),
    SUSPEND(0, "stream.suspend"),
    ;
    private final int code;
    private final String i18n;

    SprayDataStreamStatus(int code, String i18n) {
        this.code = code;
        this.i18n = i18n;
    }

    @Override
    public Class<? extends SprayResourceBundleDef> i18nClass(TargetType target) {
        switch (target) {
            case NAME -> {
                return SprayStreamStatusName_i18n.class;
            }
            case DESCRIPTION -> {
                return SprayStreamStatusComment_i18n.class;
            }
            default -> {
                return unsupported("unknown type message target, please contact with the developer");
            }
        }
    }

    @Override
    public int getCode() {
        return this.code;
    }


    @Override
    public String i18nKey() {
        return this.i18n;
    }

}
