package top.spray.db.connection.action.dma;

import top.spray.db.connection.action.SprayDataActionResult;

public interface SprayDataModificationResult extends SprayDataActionResult<SprayDataModificationAction> {
    long getAffectedRows();
}
