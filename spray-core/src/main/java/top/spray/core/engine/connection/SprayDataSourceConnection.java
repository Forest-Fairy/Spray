package top.spray.core.engine.connection;

import top.spray.core.engine.props.SprayData;
import top.spray.core.engine.props.SprayPageData;
import top.spray.core.engine.props.SprayQueryParams;

import java.util.Iterator;
import java.util.List;

public interface SprayDataSourceConnection<Conn> extends SprayConnection<Conn> {
    @Override
    Conn getOriginalConnection();
    @Override
    void close() throws Exception;

    /**
     * query data by page
     * @return a spray data iterator for the result data iterator
     * @throws Exception e
     */
    SprayPageData<?> readPage(int page, int pageSize, SprayQueryParams queryParams) throws Exception;

    Iterator<SprayData> query(int page, int pageSize, SprayQueryParams queryParams) throws Exception;

    boolean insert(SprayData sprayData, boolean commit) throws Exception;
    boolean update(SprayData sprayData, SprayQueryParams queryParams, boolean commit) throws Exception;
    boolean delete(SprayQueryParams queryParams, boolean commit) throws Exception;

}
