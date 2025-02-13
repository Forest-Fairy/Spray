package top.spray.db.connection.connection.jdbc;

import cn.hutool.db.StatementUtil;
import cn.hutool.db.sql.NamedSql;
import top.spray.db.connection.action.SprayActionHandler;
import top.spray.db.connection.action.SprayDataAction;
import top.spray.db.connection.action.SprayDataActionResult;
import top.spray.db.connection.connection.SprayConnection;
import top.spray.db.connection.exception.SpraySqlException;
import top.spray.db.connection.support.SprayTransactionSupportConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;

public class SprayJDBCConnection extends SprayTransactionSupportConnection<Connection> {

    public SprayJDBCConnection(Connection connection) {
        super(connection);
    }

    @Override
    protected <Action extends SprayDataAction<Result, ?>, Result extends SprayDataActionResult<Action>> SprayActionHandler<SprayConnection<?>, Action, Result> getActionHandler(Action action) {
        return null;
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
