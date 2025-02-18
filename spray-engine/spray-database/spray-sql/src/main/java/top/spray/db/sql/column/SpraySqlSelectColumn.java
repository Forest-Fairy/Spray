package top.spray.db.sql.column;

public interface SpraySqlSelectColumn extends SpraySqlColumn {
    Class<?> javaType();
    int jdbcType();
    String alias();
}
