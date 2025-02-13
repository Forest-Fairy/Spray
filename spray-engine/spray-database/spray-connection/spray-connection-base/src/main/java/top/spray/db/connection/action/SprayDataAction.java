package top.spray.db.connection.action;

import top.spray.core.system.type.SprayTypeI18nType;
import top.spray.db.sql.objects.db.SprayDatabaseType;

import java.util.Map;

public interface SprayDataAction<Result extends SprayDataActionResult<?>, TYPE extends SprayDataAction.Type> {
    /**
     * witch the connection will switch its catalog or schema to
     *  {{catalog}}.schema  means keep catalog but switch to schema
     *  catalog.{{schema}}  means switch to catalog but keep schema
     *  catalog.schema      means switch to catalog and schema
     */
    String database();

    SprayDatabaseType getDatabaseType();

    TYPE getActionType();

    String toParameterizedSql();

    Map<String, Object> params();

    default void callBack(Result result, Throwable throwable) {}

    interface Type extends SprayTypeI18nType {
    }
}
