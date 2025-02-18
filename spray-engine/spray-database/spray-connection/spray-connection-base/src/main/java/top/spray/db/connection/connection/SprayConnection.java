package top.spray.db.connection.connection;

import top.spray.common.tools.SprayUnsupportedOperation;
import top.spray.db.connection.action.SprayActionHandler;
import top.spray.db.connection.action.SprayDataAction;
import top.spray.db.connection.action.SprayDataActionResult;
import top.spray.db.connection.action.dqa.SprayDataQueryAction;
import top.spray.db.connection.exception.SprayDatabaseException;
import top.spray.db.sql.db.types.SprayDatabaseType;

public abstract class SprayConnection<Connection> implements AutoCloseable {

    private final SprayDatabaseType databaseType;
    private final Connection connection;
    private boolean readOnly;

    protected SprayConnection(SprayDatabaseType databaseType, Connection connection) {
        this.databaseType = databaseType;
        this.connection = connection;
    }
    /**
     * @return null means failed
     */
    public Connection getConnection() {
        return connection;
    }
    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }
    public SprayDatabaseType getDatabaseType() {
        return databaseType;
    }

    public abstract String getCatalog();
    public abstract String getSchema();

    public final <Action extends SprayDataAction<Result, ?>, Result extends SprayDataActionResult<Action>> Result accept(Action action) throws Exception {
        if (readOnly) {
            if (! (action instanceof SprayDataQueryAction)) {
                throw new SprayDatabaseException("connection.readonly.error");
            }
        }
        SprayActionHandler<SprayConnection<?>, Action, Result> handler = this.getActionHandler(action);
        return handler == null
                ? SprayUnsupportedOperation.unsupported()
                : handler.handle(this, action);
    }

    protected abstract <Action extends SprayDataAction<Result, ?>, Result extends SprayDataActionResult<Action>>
        SprayActionHandler<SprayConnection<?>, Action, Result> getActionHandler(Action action);

}
