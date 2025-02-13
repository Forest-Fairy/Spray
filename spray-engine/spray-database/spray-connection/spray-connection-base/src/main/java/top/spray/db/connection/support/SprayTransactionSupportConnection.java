package top.spray.db.connection.support;

import top.spray.db.connection.connection.SprayConnection;

public abstract class SprayTransactionSupportConnection<Connection> extends SprayConnection<Connection> {
    protected SprayTransactionSupportConnection(Connection connection) {
        super(connection);
    }

    public abstract void begin() throws Exception;

    public abstract  void commit() throws Exception;

    public abstract void rollback() throws Exception;
}
