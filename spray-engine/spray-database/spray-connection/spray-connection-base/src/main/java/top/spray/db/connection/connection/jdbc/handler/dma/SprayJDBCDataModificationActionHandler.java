package top.spray.db.connection.connection.jdbc.handler.dma;

import top.spray.db.connection.action.SprayAutoRegisterActionHandler;
import top.spray.db.connection.action.dma.SprayDataModificationAction;
import top.spray.db.connection.action.dma.SprayDataModificationResult;
import top.spray.db.connection.connection.jdbc.SprayJDBCConnection;

import java.sql.PreparedStatement;

public abstract class SprayJDBCDataModificationActionHandler extends SprayAutoRegisterActionHandler<SprayJDBCConnection, SprayDataModificationAction, SprayDataModificationResult> {
    protected SprayJDBCDataModificationActionHandler() {
        super();
    }

    protected abstract PreparedStatement getPreparedStatement(String parameterizedSql, SprayJDBCConnection connection, SprayDataModificationAction action) throws Exception;

    @Override
    protected SprayDataModificationResult doHandle(String parameterizedSql, SprayJDBCConnection connection, SprayDataModificationAction action) throws Exception {
        try (PreparedStatement preparedStatement = getPreparedStatement(parameterizedSql, connection, action)) {
            int affectedRows = preparedStatement.executeUpdate();
            return new SprayDataModificationResult() {
                @Override
                public SprayDataModificationAction getAction() {
                    return action;
                }

                @Override
                public long getAffectedRows() {
                    return affectedRows;
                }
            };
        }
    }
}