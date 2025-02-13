package top.spray.db.connection.action.dma;

import top.spray.core.global.prop.SprayUnsupportedOperation;
import top.spray.db.connection.action.SprayDataAction;
import top.spray.db.connection.exception.SprayDatabaseException;

import java.util.List;
import java.util.Map;

public interface SprayDataModificationAction extends SprayDataAction<SprayDataModificationResult, SprayDataModificationType> {
    List<Map<String, Object>> paramsList();
    @Override
    default Map<String, Object> params() {
        return SprayUnsupportedOperation.unsupported(SprayDatabaseException::new,
                "unsupported operation on SprayDataModificationAction");
    }
}
