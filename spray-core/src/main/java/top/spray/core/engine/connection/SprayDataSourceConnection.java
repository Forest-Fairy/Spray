package top.spray.core.engine.connection;

import top.spray.core.engine.props.SprayPageData;

public interface SprayDataSourceConnection<T, E> extends SprayConnection<T>{
    @Override
    T getOriginalConnection();
    @Override
    void close() throws Exception;

    SprayPageData<E> readPage(int page, int pageSize, Object... args) throws Exception;

}
