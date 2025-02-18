package top.spray.db.connection.action.dqa;

import top.spray.core.stream.SprayDataStream;
import top.spray.db.connection.action.SprayDataActionResult;

public interface SprayDataQueryResult extends SprayDataActionResult<SprayDataQueryAction> {
    SprayDataStream getDataStream();
}
