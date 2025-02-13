package top.spray.db.sql.table.query;

import top.spray.db.sql.column.SpraySqlSelectColumn;
import top.spray.db.sql.condition.SpraySqlCondition;
import top.spray.db.sql.table.SpraySqlTable;

import java.util.List;

public interface SpraySqlQueryTable extends SpraySqlTable {

    SpraySqlSelectColumn selections();

    SpraySqlTable table();

    List<SpraySqlQueryTable> joins();

    List<SpraySqlCondition> conditions();

    QueryType queryType();

    enum QueryType {
        MAIN_FROM,
        INNER_JOIN,
        LEFT_JOIN,
        RIGHT_JOIN,
        FULL_JOIN,
        UNION_ALL,
        ;
    }

}
