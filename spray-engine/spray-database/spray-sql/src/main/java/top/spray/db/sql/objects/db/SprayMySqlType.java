package top.spray.db.sql.objects.db;

public class SprayMySqlType extends SprayDatabaseType {
    public static final SprayMySqlType INSTANCE = new SprayMySqlType();
    protected SprayMySqlType() {
        super("mysql", "mysqldb");
    }

    private static final char[] ESCAPE = {'`'};
    @Override
    protected char[] escape() {
        return ESCAPE;
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
