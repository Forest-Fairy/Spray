package top.spray.db.connection.action.dqa;

import top.spray.db.connection.action.SprayDataAction;

public enum SprayDataQueryType implements SprayDataAction.Type {
    TABLE("table", 1),
    VIEW("view", 2),
    PROCEDURE("procedure", 3),
    SQL("sql", 4),
    ;

    private final String i18nKey;
    private final int code;
    SprayDataQueryType(String i18nKey, int code) {
        this.i18nKey = i18nKey;
        this.code = code;
    }
    @Override
    public String i18nKey() {
        return i18nKey;
    }

    @Override
    public int getCode() {
        return code;
    }
}
