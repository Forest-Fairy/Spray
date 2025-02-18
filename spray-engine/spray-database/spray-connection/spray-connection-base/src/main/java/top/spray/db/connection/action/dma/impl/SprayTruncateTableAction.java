package top.spray.db.connection.action.dma.impl;

import top.spray.db.connection.action.dma.SprayDataModificationAction;
import top.spray.db.connection.action.dma.SprayDataModificationType;
import top.spray.db.sql.objects.db.SprayDatabaseType;

import java.util.List;
import java.util.Map;

public class SprayTruncateTableAction implements SprayDataModificationAction {
    private final String catalog;
    private final String schema;
    private final SprayDatabaseType databaseType;
    private final String tableName;

    public SprayTruncateTableAction(String catalog, String schema, SprayDatabaseType databaseType, String tableName) {
        this.catalog = catalog;
        this.schema = schema;
        this.databaseType = databaseType;
        this.tableName = tableName;
    }

    @Override
    public List<Map<String, Object>> paramsList() {
        return List.of();
    }

    @Override
    public String catalog() {
        return catalog;
    }

    @Override
    public String schema() {
        return schema;
    }

    @Override
    public SprayDatabaseType getDatabaseType() {
        return databaseType;
    }

    @Override
    public SprayDataModificationType getActionType() {
        return SprayDataModificationType.TRUNCATE;
    }

    @Override
    public String toParameterizedSql() {
        return "truncate table " + databaseType.doEscape(tableName);
    }
}
