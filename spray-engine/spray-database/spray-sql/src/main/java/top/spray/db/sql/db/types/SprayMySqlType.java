package top.spray.db.sql.db.types;

public class SprayMySqlType extends SprayDatabaseType {
    public static final SprayMySqlType INSTANCE = new SprayMySqlType();
    protected SprayMySqlType() {
        super("mysql", "mysqldb");
    }

    private static final char[] ESCAPE = {'`'};
    @Override
    public char[] escape() {
        return ESCAPE;
    }

    @Override
    public String escapeTableName(boolean doOrRemoveEscape, String catalog, String schema, String tableName) {
        return super.escapeTableName(doOrRemoveEscape, catalog, null, tableName);
    }

    @Override
    public String castToTypeToken(Class<?> c) {
        return null;
    }

    @Override
    public Class<?> castToJavaType(int jdbcType) {
        return null;
    }

    @Override
    public String castToDatabaseTypeToken(String typeToken, SprayDatabaseType asType) {
        return "";
    }

    @Override
    public String formatValue(String value, String typeToken) {
        if (typeToken.contains("char")) {
            value = "'" + value + "'";
        }
        return value;
    }
}
