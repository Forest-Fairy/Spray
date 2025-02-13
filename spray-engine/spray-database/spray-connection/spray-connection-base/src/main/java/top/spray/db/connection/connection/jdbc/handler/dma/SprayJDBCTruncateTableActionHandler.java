package top.spray.db.connection.connection.jdbc.handler.dma;

import top.spray.common.tools.Sprays;
import top.spray.db.connection.action.SprayDataAction;
import top.spray.db.connection.action.dma.SprayDataModificationAction;
import top.spray.db.connection.action.dma.SprayDataModificationType;
import top.spray.db.connection.connection.jdbc.SprayJDBCConnection;

import java.sql.PreparedStatement;

public class SprayJDBCTruncateTableActionHandler extends SprayJDBCDataModificationActionHandler {
    protected SprayJDBCTruncateTableActionHandler() {
        super();
    }

    @Override
    protected SprayDataAction.Type[] types() {
        return Sprays.asArray(SprayDataModificationType.TRUNCATE);
    }

    @Override
    protected PreparedStatement getPreparedStatement(String parameterizedSql, SprayJDBCConnection connection, SprayDataModificationAction action) throws Exception {
        return null;
    }

    @Override
    protected void validateAction(String parameterizedSql, SprayJDBCConnection connection, SprayDataModificationAction action) throws Exception {
        String token = GetFirstToken(parameterizedSql);
        if (token.equalsIgnoreCase("truncate")) {
            return;
        }
        unsupported("unsupported operation with sql token " + token);
    }
}