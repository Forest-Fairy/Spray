package top.spray.core.stream;

import top.spray.core.type.SprayTypeI18nType;

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
    public int getCode() {
        return this.code;
    }

    @Override
    public String i18nKey() {
        return this.i18n;
    }

}
