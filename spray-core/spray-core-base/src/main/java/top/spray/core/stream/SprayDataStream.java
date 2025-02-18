package top.spray.core.stream;

import org.jetbrains.annotations.NotNull;
import top.spray.core.global.exception.SprayCoreException;
import top.spray.core.global.prop.SprayData;

import java.util.*;
import java.util.concurrent.atomic.LongAdder;
import java.util.function.Function;

public class SprayDataStream extends SprayDataProviderOwner implements SprayDataPipeLine<SprayDataStream> {

    private volatile int status;
    protected final Map<String, SprayDataIterator> iterators;
    protected final LinkedList<SprayDataProvider> dataProviders;
    protected final List<Long> providerRanges;
    protected LongAdder curLength;
    private final Map<String, String> props;


    public SprayDataStream(SprayDataProvider dataProvider, Map<String, String> props) {
        this.iterators = new HashMap<>();
        this.dataProviders = new LinkedList<>();
        this.dataProviders.add(dataProvider);
        this.providerRanges = new LinkedList<>();
        this.providerRanges.add(0L);
        this.curLength = new LongAdder();
        this.curLength.add(dataProvider.curSize());
        this.props = props;
    }
    public SprayDataStream(SprayData data, Map<String, String> props) {
        this.iterators = new HashMap<>();
        this.dataProviders = new LinkedList<>();
        DefaultDataProvider dataProvider = new DefaultDataProvider();
        dataProvider.add(data);
        this.dataProviders.add(dataProvider);
        this.providerRanges = new LinkedList<>();
        this.providerRanges.add(0L);
        this.curLength = new LongAdder();
        this.curLength.add(dataProvider.curSize());
        this.props = props;
    }

    @Override
    public List<SprayDataProvider> providers() {
        return Collections.unmodifiableList(this.dataProviders);
    }

    @Override
    public List<Long> providerRanges() {
        return Collections.unmodifiableList(this.providerRanges);
    }

    private void createNewProvider() {
        this.providerRanges.add(this.providerRanges.get(this.providerRanges.size() - 1) + this.dataProviders.getLast().curSize());
        this.dataProviders.add(new DefaultDataProvider());
    }

    @Override
    public SprayDataStream push(SprayData data) {
        DefaultDataProvider tmp = new DefaultDataProvider();
        tmp.add(data);
        return pushMany(tmp);
    }

    @Override
    public SprayDataStream pushMany(Collection<SprayData> many) {
        DefaultDataProvider tmp = new DefaultDataProvider();
        tmp.addAll(many);
        return pushMany(tmp);
    }

    @Override
    public SprayDataStream pushMany(SprayDataProvider provider) {
        while (this.isClosed()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new SprayCoreException("thread interrupted");
            }
        }
        errorIfDone();
        if (! this.dataProviders.getLast().addableFor(provider.curSize())) {
            createNewProvider();
        }
        this.dataProviders.getLast().addAll(provider);
        this.curLength.add(provider.curSize());
        return this;
    }

    @Override
    public @NotNull SprayDataIterator cachedIterator(String key, Function<SprayDataStream, SprayDataIterator> iteratorSupplier) {
        return iterators.computeIfAbsent(key, k -> iteratorSupplier.apply(this));
    }

    @Override
    public @NotNull SprayDataIterator notCachedIterator(Function<SprayDataStream, SprayDataIterator> iteratorSupplier) {
        return iteratorSupplier.apply(this);
    }

    @Override
    public String getProperty(String key) {
        return props.get(key);
    }

    protected void errorIfDone() {
        if (this.isDone()) {
            throw new IllegalStateException("stream is done, can not push data anymore");
        }
    }

    @Override
    public long curLength() {
        return curLength.longValue();
    }

    @Override
    public boolean isDone() {
        return this.status == -1;
    }

    @Override
    public boolean isClosed() {
        return this.status == 0;
    }

    @Override
    public boolean isOpen() {
        return this.status == 1;
    }

    @Override
    public void close() throws Exception {
        errorIfDone();
        this.status = 0;
    }

    public void open() {
        errorIfDone();
        this.status = 1;
    }

    public void done() {
        this.status = -1;
    }

    public static DefaultDataProvider newDataProvider() {
        return new DefaultDataProvider();
    }
    public static class DefaultDataProvider implements SprayDataProvider {
        private final LinkedList<SprayData> list;
        private final LongAdder counter;

        private DefaultDataProvider() {
            this.counter = new LongAdder();
            this.list = new LinkedList<>();
        }

        @Override
        public int curSize() {
            return this.counter.intValue();
        }

        @Override
        public boolean addableFor(int willAddSize) {
            return (this.list.size() + willAddSize) < Integer.MAX_VALUE;
        }

        @Override
        public void add(SprayData data) {
            this.list.add(data);
            this.counter.increment();
        }

        @Override
        public void addAll(Collection<SprayData> all) {
            this.list.addAll(all);
            this.counter.add(all.size());
        }

        @Override
        public void addAll(SprayDataProvider all) {
            all.iterator().forEachRemaining(this::add);
            this.counter.add(all.curSize());
        }

        @Override
        public @NotNull Iterator<SprayData> iterator() {
            return list.iterator();
        }
    }

}
