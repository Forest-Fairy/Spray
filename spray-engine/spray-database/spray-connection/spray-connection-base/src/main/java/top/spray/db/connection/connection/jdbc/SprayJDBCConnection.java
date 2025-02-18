package top.spray.db.connection.connection.jdbc;

import cn.hutool.db.StatementUtil;
import cn.hutool.db.sql.NamedSql;
import top.spray.common.tools.SprayTester;
import top.spray.db.connection.action.SprayActionHandler;
import top.spray.db.connection.action.SprayAutoRegisterActionHandler;
import top.spray.db.connection.action.SprayDataAction;
import top.spray.db.connection.action.SprayDataActionResult;
import top.spray.db.connection.connection.SprayConnection;
import top.spray.db.connection.exception.SpraySqlException;
import top.spray.db.connection.support.SprayTransactionSupportConnection;
import top.spray.db.sql.db.types.SprayDatabaseType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;

public class SprayJDBCConnection extends SprayTransactionSupportConnection<Connection> {
    public SprayJDBCConnection(SprayDatabaseType databaseType, Connection connection) {
        super(databaseType, connection);
    }

    @Override
    public String getCatalog() {
        return SprayTester.supply(() -> this.getConnection().getCatalog(), null);
    }

    @Override
    public String getSchema() {
        return SprayTester.supply(() -> this.getConnection().getSchema(), null);
    }

    @Override
    protected <Action extends SprayDataAction<Result, ?>, Result extends SprayDataActionResult<Action>> SprayActionHandler<SprayConnection<?>, Action, Result> getActionHandler(Action action) {
        return SprayAutoRegisterActionHandler.getInstance(SprayJDBCConnection.class, action);
    }

    @Override
    public void close() throws SQLException {
        if (! this.getConnection().isClosed()) {
            this.getConnection().close();
        }
    }

    public PreparedStatement getPreparedStatement(String parameterizedSql, Map<String, Object> params) {
        try {
            NamedSql namedSql = new NamedSql(parameterizedSql, params);
            return StatementUtil.prepareStatement(this.getConnection(), namedSql.getSql(), namedSql.getParams());
        } catch (SQLException e) {
            throw new SpraySqlException(e, "jdbc.sql.error", parameterizedSql, e.getMessage());
        }
    }

    @Override
    public void begin() throws Exception {
        this.getConnection().setAutoCommit(false);
    }

    @Override
    public void commit() throws Exception {
        this.getConnection().commit();
    }

    @Override
    public void rollback() throws Exception {
        this.getConnection().rollback();
    }
}
