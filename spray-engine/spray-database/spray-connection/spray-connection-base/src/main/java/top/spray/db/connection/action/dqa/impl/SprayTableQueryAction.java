package top.spray.db.connection.action.dqa.impl;

import top.spray.common.tools.tuple.SprayTuples;
import top.spray.db.connection.action.dqa.SprayDataQueryAction;
import top.spray.db.connection.action.dqa.SprayDataQueryType;
import top.spray.db.sql.objects.db.SprayDatabaseType;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class SprayTableQueryAction implements SprayDataQueryAction {
    private final SprayDatabaseType dbType;
    private final List<String> selectFields;
    private final Map<String, Function<Object, Object>> castors;
    private final String catalog;
    private final String schema;
    private final String queryTable;
    private final String extraWhere;
    // less than zero means DESC
    private final List<SprayTuples._2<String, Long>> orderBy;
    private final String extraOrderByAndGroupBy;
    private final Map<String, Object> params;

    private SprayTableQueryAction(SprayDatabaseType dbType,
                                  String[] selectFields,
                                  Map<String, Function<Object, Object>> castors,
                                  String catalog,
                                  String schema,
                                  String queryTable,
                                  String extraWhere,
                                  List<SprayTuples._2<String, Long>> orderBy,
                                  String extraOrderByAndGroupBy,
                                  Map<String, Object> params) {
        this.dbType = dbType;
        this.selectFields = List.of(selectFields);
        this.castors = castors;
        this.catalog = catalog;
        this.schema = schema;
        this.queryTable = queryTable;
        this.extraWhere = extraWhere;
        this.orderBy = orderBy;
        this.extraOrderByAndGroupBy = extraOrderByAndGroupBy;
        this.params = (params == null || params.isEmpty())
                ? Collections.emptyMap()
                : new HashMap<>(params);
    }
    @Override
    public List<String> selectFields() {
        return selectFields;
    }

    @Override
    public Map<String, Function<Object, Object>> castors() {
        return castors;
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
        return dbType;
    }

    @Override
    public SprayDataQueryType getActionType() {
        return SprayDataQueryType.TABLE;
    }

    @Override
    public String toParameterizedSql() {
        // TODO Combine with sql definition in spray-core
        return ;
    }

    @Override
    public Map<String, Object> params() {
        return params;
    }

}
