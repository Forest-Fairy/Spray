package top.spray.db.sql.table.create;

import top.spray.db.sql.table.SpraySqlTableConfiguration;
import top.spray.db.sql.column.SpraySqlColumnDefinition;
import top.spray.db.sql.table.SpraySqlNamedTable;
import top.spray.db.sql.table.SpraySqlTable;

import java.util.List;

public interface SpraySqlCreateTable extends SpraySqlTable {
    SpraySqlNamedTable tableName();
    List<SpraySqlColumnDefinition> columns();
    List<SpraySqlTableConfiguration> configurations();
}
