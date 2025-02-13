package top.spray.db.connection.action.dma;

import top.spray.db.connection.action.SprayDataAction;

public enum SprayDataModificationType implements SprayDataAction.Type {
    UPSERT("upsert", 1),
    UPDATE_ONLY("update_only", 2),
    INSERT_ONLY("insert_only", 3),
    DELETE("delete", 4),
    TRUNCATE("truncate", 5),
    ;

    private final String i18nKey;
    private final int code;
    SprayDataModificationType(String i18nKey, int code) {
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
