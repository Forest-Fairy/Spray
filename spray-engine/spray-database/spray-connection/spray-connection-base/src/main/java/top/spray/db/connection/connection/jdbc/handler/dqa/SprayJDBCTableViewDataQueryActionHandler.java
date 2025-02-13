package top.spray.db.connection.connection.jdbc.handler.dqa;

import top.spray.common.tools.Sprays;
import top.spray.db.connection.action.SprayDataAction;
import top.spray.db.connection.action.dqa.SprayDataQueryAction;
import top.spray.db.connection.action.dqa.SprayDataQueryType;
import top.spray.db.connection.connection.jdbc.SprayJDBCConnection;

import java.sql.PreparedStatement;

public class SprayJDBCTableViewDataQueryActionHandler extends SprayJDBCDataQueryActionHandler {
    private static final SprayJDBCTableViewDataQueryActionHandler INSTANCE = new SprayJDBCTableViewDataQueryActionHandler();
    private SprayJDBCTableViewDataQueryActionHandler() {
        super();
    }

    @Override
    protected SprayDataAction.Type[] types() {
        return Sprays.asArray(SprayDataQueryType.TABLE, SprayDataQueryType.VIEW);
    }

    @Override
    protected PreparedStatement getPreparedStatement(String parameterizedSql, SprayJDBCConnection connection, SprayDataQueryAction action) throws Exception {
        String sql = String.format("select %s from (%s)",
                String.join(",", action.selectFields()), parameterizedSql);
        return connection.getPreparedStatement(sql, action.params());
    }

    @Override
    protected void validateAction(String parameterizedSql, SprayJDBCConnection connection, SprayDataQueryAction action) throws Exception {
        String token = GetFirstToken(parameterizedSql);
        if (token.equalsIgnoreCase("select")
                || token.equalsIgnoreCase("describe")) {
            if (action.selectFields() != null && !action.selectFields().isEmpty()) {
                return;
            }
            unsupported("selectFields is required when query table or view");
        }
        unsupported("unsupported operation with sql token " + token);
    }
}
