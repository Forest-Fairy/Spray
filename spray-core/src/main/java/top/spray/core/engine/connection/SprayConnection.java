package top.spray.core.engine.connection;

public interface SprayConnection<T> {
    T getOriginalConnection();
    void close() throws Exception;
}
