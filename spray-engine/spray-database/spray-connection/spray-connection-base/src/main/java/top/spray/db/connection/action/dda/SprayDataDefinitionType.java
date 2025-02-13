package top.spray.db.connection.action.dda;

import top.spray.db.connection.action.SprayDataAction;

public enum SprayDataDefinitionType implements SprayDataAction.Type {
    CREATE_DATABASE("create_database", 10),
    CREATE_TABLE("create_table", 11),
    DROP_TABLE("drop_table", 12),
    CREATE_VIEW("create_view", 13),
    DROP_VIEW("drop_view", 14),
    ADD_COLUMN("add_column", 21),
    DROP_COLUMN("drop_column", 21),
    ADD_CONSTRAINT("add_constraint", 31),
    DROP_CONSTRAINT("drop_constraint", 32),
    ;

    private final String i18nKey;
    private final int code;
    SprayDataDefinitionType(String i18nKey, int code) {
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
