package top.spray.core.engine.props;

import cn.hutool.core.util.TypeUtil;

import java.util.Iterator;
import java.util.function.Consumer;
import java.util.function.Function;

public class SprayPageData<T> implements Iterator<SprayData>, Iterable<SprayData> {
    private final Iterator<T> iterator;
    private final Function<T, SprayData> castor;
    private final Class<T> itClass;
    private final int pageNum;
    private final int pageSize;
    private final long total;
    private final long totalPage;

    private SprayPageData(Iterator<T> iterator, Function<T, SprayData> castor, Class<T> itClass, int pageNum, int pageSize, long total, long totalPage) {
        this.iterator = iterator;
        this.itClass = itClass;
        this.castor = castor;
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.total = total;
        this.totalPage = totalPage;
    }

    @Override
    public boolean hasNext() {
        return iterator.hasNext();
    }
    @Override
    public SprayData next() {
        return castor.apply(iterator.next());
    }

    @Override
    public void remove() {
        iterator.remove();
    }

    @Override
    public void forEachRemaining(Consumer<? super SprayData> action) {
        iterator.forEachRemaining(t -> action.accept(castor.apply(t)));
    }
    
    private void skip() {
        if (iterator == null || !iterator.hasNext()) {
            return;
        }
        if (pageNum < 2) {
            return;
        }
        long offset = (pageNum - 1L) * pageSize;
        while (iterator.hasNext() && offset-- > 0L) {
            iterator.next();
        }
    }

    @Override
    public Iterator<SprayData> iterator() {
        return this;
    }

    public int getPageNum() {
        return pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public long getTotal() {
        return total;
    }

    public long getTotalPage() {
        return totalPage;
    }

    public Class<T> originalType() {
        return itClass;
    }

    /**
     * build a spray page data object
     * @param iterator the original iterator
     * @param castor cast the original data to spray data
     * @param skipOffset true if the iterator need to skip offset
     * @return a SprayPageData object
     * @param <T> original data object
     */
    public static <T> SprayPageData<T> build(Iterator<T> iterator, int pageNum, int pageSize, long total, Function<T, SprayData> castor, boolean skipOffset) {
        if (pageNum < 1 || pageSize < 1) {
            throw new IllegalArgumentException("page num or page size should be greater than zero!");
        }
        Class<?> itClass = TypeUtil.getClass(TypeUtil.getTypeArgument(iterator.getClass()));
        SprayPageData<T> pageData = new SprayPageData<>(iterator, castor, (Class<T>) itClass, pageNum, pageSize, total,
                total / pageSize + (total % pageSize == 0L ? 0L : 1L));
        if (skipOffset) {
            pageData.skip();
        }
        return pageData;
    }
}
