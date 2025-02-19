package top.spray.db.connection.action;

import top.spray.core.type.SprayType;
import top.spray.db.connection.exception.SprayDatabaseException;
import top.spray.db.sql.db.types.SprayDatabaseType;

import java.util.Map;

public interface SprayDataAction<Result extends SprayDataActionResult<?>, TYPE extends SprayDataAction.Type> {

    String catalog();

    String schema();

    SprayDatabaseType getDatabaseType();

    TYPE getActionType();

    String toParameterizedSql();

    Map<String, Object> params();

    default void callBackHandled(Result result) {}
    default void callBackError(Throwable throwable) {
        throw new SprayDatabaseException(throwable);
    }

    interface Type extends SprayType {
    }
}
