package top.spray.core.engine.props;

import lombok.Builder;
import lombok.Data;

import java.util.Iterator;

@Data
public class SprayPageData<T> implements Iterator<T> {
    private Iterator<T> iterator;
    private int pageNum;
    private int pageSize;
    private long total;
    private long totalPage;

    @Override
    public boolean hasNext() {
        return iterator.hasNext();
    }

    @Override
    public T next() {
        return iterator.next();
    }

    public static <T> SprayPageData<T> build(Iterator<T> iterator, int pageNum, int pageSize, long total) {
        SprayPageData<T> sprayPageData = new SprayPageData<>();
        sprayPageData.setIterator(iterator);
        sprayPageData.setPageNum(pageNum);
        sprayPageData.setPageSize(pageSize);
        sprayPageData.setTotal(total);
        sprayPageData.setTotalPage(total / pageSize +
                (total % pageSize == 0L ? 0L : 1L));
        return sprayPageData;
    }
}
