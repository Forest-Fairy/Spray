package top.spray.db.connection.connection.jdbc.handler.dqa;

import top.spray.common.tools.Sprays;
import top.spray.db.connection.action.SprayDataAction;
import top.spray.db.connection.action.dqa.SprayDataQueryAction;
import top.spray.db.connection.action.dqa.SprayDataQueryType;
import top.spray.db.connection.connection.jdbc.SprayJDBCConnection;

import java.sql.PreparedStatement;

public class SprayJDBCSqlDataQueryActionHandler extends SprayJDBCDataQueryActionHandler {
    private static final SprayJDBCSqlDataQueryActionHandler INSTANCE = new SprayJDBCSqlDataQueryActionHandler();
    private SprayJDBCSqlDataQueryActionHandler() {
        super();
    }

    @Override
    protected SprayDataAction.Type[] types() {
        return Sprays.asArray(SprayDataQueryType.TABLE, SprayDataQueryType.VIEW);
    }

    @Override
    protected PreparedStatement getPreparedStatement(String parameterizedSql, SprayJDBCConnection connection, SprayDataQueryAction action) throws Exception {
        String sql = parameterizedSql;
        if (GetFirstToken(sql).equalsIgnoreCase("select") && action.selectFields() != null && !action.selectFields().isEmpty()) {
            sql = String.format("select %s from (%s)",
                    String.join(",", action.selectFields()), sql);
        }
        return connection.getPreparedStatement(sql, action.params());
    }

    @Override
    protected void validateAction(String parameterizedSql, SprayJDBCConnection connection, SprayDataQueryAction action) throws Exception {
        String token = GetFirstToken(parameterizedSql);
        if (token.equalsIgnoreCase("select")
                || token.equalsIgnoreCase("describe")) {
            return;
        }
        unsupported("unsupported operation with sql token " + token);
    }
}
