package top.spray.db.sql.db.types;

import top.spray.db.sql.objects.SprayJdbcTypeMapping;

import java.util.HashMap;
import java.util.Map;

public abstract class SprayDatabaseType {
    private static final Map<String, SprayDatabaseType> types = new HashMap<>();
    private static SprayDatabaseType get(String productName) {
        return types.get(productName);
    }

    private final String name;
    private final String product;

    protected SprayDatabaseType(String name, String product) {
        this.name = name.toLowerCase();
        this.product = product;
        types.put(this.product, this);
    }

    public String getName() {
        return name;
    }

    public String getProduct() {
        return product;
    }

    public String escapeTableName(boolean doOrRemoveEscape, String catalog, String schema, String tableName) {
        return escapeAndAppend(doOrRemoveEscape, tableName, schema, catalog);
    }

    public String handleColumnEscape(boolean doOrRemoveEscape, String tableName, String colName) {
        return escapeAndAppend(doOrRemoveEscape, colName, tableName);
    }

    protected String escapeAndAppend(boolean doOrRemoveEscape, String name, String... names) {
        if (name == null || name.isBlank()) {
            return "";
        }
        name = handleEscape(doOrRemoveEscape, name);
        if (names != null && names.length > 0) {
            StringBuilder nameBuilder = new StringBuilder(name);
            for (String n : names) {
                if (n != null && !n.isBlank()) {
                    n = handleEscape(doOrRemoveEscape, n) + ".";
                    if (!nameBuilder.toString().contains(n)) {
                        nameBuilder.insert(0, n);
                    }
                }
            }
            name = nameBuilder.toString();
        }
        return name;
    }
    private String handleEscape(boolean doOrRemoveEscape, String name) {
        if (name == null || name.trim().isEmpty()) {
            return "";
        }
        char[] escape = escape();
        String left = String.valueOf(escape[0]);
        String right = String.valueOf(escape.length > 1 ? escape[1] : escape[0]);
        StringBuilder sb = new StringBuilder();
        for (String n : name.split(",")) {
            boolean escaped = n.startsWith(left) && n.endsWith(right);
            if (doOrRemoveEscape) {
                if (escaped) {
                    sb.append(n);
                } else {
                    sb.append(left).append(n).append(right);
                }
            } else {
                // remove escape
                if (escaped) {
                    sb.append(n, 1, n.length() - 1);
                } else {
                    sb.append(n);
                }
            }
            sb.append(",");
        }
        return sb.substring(0, sb.length() - 1);
    }

    /**
     * escape char array, length is only 1 or 2, it means left and right while the length is 2
     */
    public abstract char[] escape();

    /**
     * cast java type to current db type
     * @return if null then follow {@link SprayJdbcTypeMapping#toJdbcType(Class)}
     */
    public abstract String castToTypeToken(Class<?> c);

    /**
     * cast current jdbc type to java type
     * @return if null then follow {@link SprayJdbcTypeMapping#toJavaType(int)}
     */
    public abstract Class<?> castToJavaType(int jdbcType);

    /**
     * cast current db type to other db type
     * @return if null means not support else if empty means original
     */
    public abstract String castToDatabaseTypeToken(String typeToken, SprayDatabaseType asType);

    /**
     * format value to current db type
     * @param value such as 1, hello, 2024-01-01, etc.
     * @param typeToken such as INT, VARCHAR, DATE, etc.
     * @return formatted value such as 1, 'hello', '2024-01-01', etc.
     */
    public abstract String formatValue(String value, String typeToken);

}
