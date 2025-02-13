package top.spray.db.connection.connection;

import top.spray.core.global.prop.SprayUnsupportedOperation;
import top.spray.db.connection.action.SprayActionHandler;
import top.spray.db.connection.action.SprayDataAction;
import top.spray.db.connection.action.SprayDataActionResult;
import top.spray.db.connection.action.dqa.SprayDataQueryAction;
import top.spray.db.connection.exception.SprayDatabaseException;

public abstract class SprayConnection<Connection> implements AutoCloseable {

    private final Connection connection;
    private boolean readOnly;

    protected SprayConnection(Connection connection) {
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

    public final <Conn extends SprayConnection<?>, Action extends SprayDataAction<Result, ?>, Result extends SprayDataActionResult<Action>> Result accept(Action action) throws Exception {
        if (readOnly) {
            if (! (action instanceof SprayDataQueryAction)) {
                throw new SprayDatabaseException("connection.readonly.error");
            }
        }
        SprayActionHandler<Conn, Action, Result> handler =
                SprayActionHandler.getInstance(this.getClass(), action);
        return handler == null
                ? SprayUnsupportedOperation.unsupported()
                : handler.handle((Conn) this, action);
    }

}
