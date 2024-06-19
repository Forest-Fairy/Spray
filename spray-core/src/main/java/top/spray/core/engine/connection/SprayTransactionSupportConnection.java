package top.spray.core.engine.connection;

public interface SprayTransactionSupportConnection<T> extends SprayConnection<T> {
    void begin() throws Exception;

    void commit() throws Exception;

    void rollback() throws Exception;
}
