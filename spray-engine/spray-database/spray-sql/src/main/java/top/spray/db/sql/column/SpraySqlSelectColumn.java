package top.spray.db.sql.column;

import top.spray.db.sql.objects.SpraySqlObject;

public interface SpraySqlSelectColumn extends SpraySqlObject {
    Class<?> javaType();
    int jdbcType();
    String alias();
}
