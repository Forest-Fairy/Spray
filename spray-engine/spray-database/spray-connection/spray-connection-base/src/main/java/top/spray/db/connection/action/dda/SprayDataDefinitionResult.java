package top.spray.db.connection.action.dda;

import top.spray.db.connection.action.SprayDataActionResult;

public interface SprayDataDefinitionResult extends SprayDataActionResult<SprayDataDefinitionAction> {
    boolean isSuccess();
}
