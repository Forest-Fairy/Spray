package top.spray.db.sql.column;

import top.spray.db.sql.constraint.SpraySqlConstraint;

import java.util.Map;

public interface SpraySqlColumnDefinition extends SpraySqlColumn {
    String typeToken();

    String lastColName();

    String comment();

    /**
     * key means the constraint type, it helps reduce the time cost
     */
    Map<String, SpraySqlConstraint> constraints();

}
