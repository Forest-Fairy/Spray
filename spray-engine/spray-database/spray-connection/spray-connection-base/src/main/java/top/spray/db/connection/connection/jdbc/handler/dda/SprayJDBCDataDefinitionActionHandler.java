package top.spray.db.connection.connection.jdbc.handler.dda;

import top.spray.db.connection.action.SprayAutoRegisterActionHandler;
import top.spray.db.connection.action.dda.SprayDataDefinitionAction;
import top.spray.db.connection.action.dda.SprayDataDefinitionResult;
import top.spray.db.connection.connection.jdbc.SprayJDBCConnection;

import java.sql.PreparedStatement;

public abstract class SprayJDBCDataDefinitionActionHandler extends SprayAutoRegisterActionHandler<SprayJDBCConnection, SprayDataDefinitionAction, SprayDataDefinitionResult> {
    protected SprayJDBCDataDefinitionActionHandler() {
        super();
    }

    protected abstract PreparedStatement getPreparedStatement(String parameterizedSql, SprayJDBCConnection connection, SprayDataDefinitionAction action) throws Exception;

    @Override
    protected SprayDataDefinitionResult doHandle(String parameterizedSql, SprayJDBCConnection connection, SprayDataDefinitionAction action) throws Exception {
        try (PreparedStatement preparedStatement = getPreparedStatement(parameterizedSql, connection, action)) {
            preparedStatement.executeUpdate();
            return new SprayDataDefinitionResult() {
                @Override
                public SprayDataDefinitionAction getAction() {
                    return action;
                }

                @Override
                public boolean isSuccess() {
                    return true;
                }
            };
        }
    }
}