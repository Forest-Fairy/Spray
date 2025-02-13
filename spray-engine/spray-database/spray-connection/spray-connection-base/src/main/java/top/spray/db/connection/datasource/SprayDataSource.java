package top.spray.db.connection.datasource;

import top.spray.db.connection.connection.SprayConnection;

public interface SprayDataSource<Conn> {
    SprayConnection<Conn> getConnection();

    void close() throws Exception;

}
