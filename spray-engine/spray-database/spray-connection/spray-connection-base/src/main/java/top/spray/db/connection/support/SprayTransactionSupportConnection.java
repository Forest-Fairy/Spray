package top.spray.db.connection.support;

import top.spray.db.connection.connection.SprayConnection;
import top.spray.db.sql.db.types.SprayDatabaseType;

public abstract class SprayTransactionSupportConnection<Connection> extends SprayConnection<Connection> {
    protected SprayTransactionSupportConnection(SprayDatabaseType databaseType, Connection connection) {
        super(databaseType, connection);
    }

    public abstract void begin() throws Exception;

    public abstract  void commit() throws Exception;

    public abstract void rollback() throws Exception;
}
