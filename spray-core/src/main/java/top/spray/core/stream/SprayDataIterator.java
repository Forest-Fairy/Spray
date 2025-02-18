package top.spray.core.stream;

import top.spray.common.data.SprayData;
import top.spray.common.tools.SprayOptional;
import top.spray.core.exception.SprayException;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.LongAdder;
import java.util.concurrent.locks.LockSupport;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class SprayDataIterator implements Iterator<SprayOptional<SprayData>> {

    private final SprayDataIterator wrapped;
    private final SprayDataStream dataStream;
    private final LongAdder readCount;
    private final Map<Integer, Iterator<SprayData>> iterators;
    private final List<Long> blockingDelays;

    private int latestIteratorIndex;

    private Supplier<? extends SprayException> interruptedExceptionSupplier;

    public SprayDataIterator(SprayDataStream dataStream) {
        this(dataStream, 0L);
    }
    public SprayDataIterator(SprayDataStream dataStream, Long startIndex) {
        this.wrapped = null;
        this.dataStream = dataStream;
        this.readCount = new LongAdder();
        this.readCount.add(startIndex);
        this.iterators = new HashMap<>();
        this.blockingDelays = new LinkedList<>();
    }
    protected SprayDataIterator(SprayDataIterator wrapped) {
        this.wrapped = wrapped;
        this.dataStream = null;
        this.readCount = null;
        this.iterators = null;
        this.blockingDelays = null;
    }
    protected boolean isWrapped() {
        return wrapped != null;
    }
    protected SprayDataIterator self() {
        if (isWrapped()) {
            return wrapped.self();
        } else {
            return this;
        }
    }
    protected SprayDataIterator wrapOrSelf() {
        if (isWrapped()) {
            return wrapped;
        } else {
            return this;
        }
    }

    /**
     * return wrap or else null
     */
    public <T extends SprayDataIterator> T getWrapType(Class<T> tClass) {
        if (tClass.isAssignableFrom(this.getClass())) {
            return (T) this;
        } else {
            if (this.isWrapped()) {
                return this.wrapped.getWrapType(tClass);
            } else {
                return null;
            }
        }
    }

    // size       16       248       12       48       325       241
    // range     0-15     16-263   264-275  276-324   325-649  650-890
    // x = 265 -> { 265 < 276 } -> { 265 > 15 } -> { 265 > 263 }
    private Iterator<SprayData> iterator() {
        if (isWrapped()) {
            return wrapOrSelf().iterator();
        }
        if (isDone()) {
            return Collections.emptyIterator();
        }
        updateIteratorIndex();
        return self().iterators.computeIfAbsent(self().latestIteratorIndex,
                index -> self().dataStream.providers().get(index).iterator());
    }

    private void updateIteratorIndex() {
        //   0,      16,    264,    276,    325,    650
        //  15,     263,    275,    324,    649
        int rangesSize = self().dataStream.providerRanges().size() - 1;
        if (self().latestIteratorIndex != rangesSize) {
            long[] ranges = new long[rangesSize];
            for (int i = self().latestIteratorIndex + 1; i < ranges.length; i++) {
                ranges[i - 1] = self().dataStream.providerRanges().get(i) - 1;
            }
            long curDataIndex = self().readCount.longValue();
            if (ranges.length > 0) {
                while (ranges[self().latestIteratorIndex] < curDataIndex) {
                    self().latestIteratorIndex++;
                    if (self().latestIteratorIndex == ranges.length) {
                        break;
                    }
                }
            }
        }
    }

    protected void yieldAndCheckIfInterrupted() {
        if (Thread.interrupted()) {
            if (self().interruptedExceptionSupplier != null) {
                throw self().interruptedExceptionSupplier.get();
            }
            throw new SprayCoreException("thread interrupted");
        }
        Thread.yield();
        LockSupport.parkNanos(TimeUnit.MILLISECONDS.toNanos(50L));
    }

    protected void setInterruptedExceptionSupplier(Supplier<? extends SprayException> interruptedExceptionSupplier) {
        self().interruptedExceptionSupplier = interruptedExceptionSupplier;
    }

    public SprayDataStream getDataStream() {
        return self().dataStream;
    }

    public int getLatestIteratorIndex() {
        return self().latestIteratorIndex;
    }

    public long readCount() {
        return self().readCount.longValue();
    }

    public boolean isDone() {
        return self().readCount.longValue() == self().dataStream.curLength() && self().dataStream.isDone();
    }

    public boolean isBlocking() {
        return self().dataStream.isClosed()
            || (self().readCount.longValue() == self().dataStream.curLength() && self().dataStream.isOpen());
    }


    public List<Long> getBlockingDelays() {
        return Collections.unmodifiableList(blockingDelays);
    }

    @Override
    public boolean hasNext() {
        if (isDone()) {
            return false;
        }
        // blocking if dataStream is closed or there is no more data temporarily
        long time = isBlocking() ? System.currentTimeMillis() : 0L;
        while (time != 0L && isBlocking()) {
            yieldAndCheckIfInterrupted();
        }
        if (time != 0L) {
            blockingDelays.add(System.currentTimeMillis() - time);
        }
        return self().iterator().hasNext();
    }

    @Override
    public final SprayOptional<SprayData> next() {
        if (isDone()) {
            throw new SprayNoMoreDataToReadException();
        }
        if (isWrapped()) {
            return overwriteNext(wrapped);
        } else {
            if (hasNext()) {
                SprayOptional<SprayData> optional = SprayOptional.of(iterator().next());
                readCount.increment();
                return optional;
            } else {
                return SprayOptional.empty();
            }
        }
    }
    protected SprayOptional<SprayData> overwriteNext(SprayDataIterator next) {
        return next.next();
    }


    @Override
    public void remove() {
        throw new UnsupportedOperationException("remove is unsupported for " + SprayDataIterator.class.getName());
    }

    @Override
    public void forEachRemaining(Consumer<? super SprayOptional<SprayData>> action) {
        while (self().hasNext()) {
            action.accept(self().next());
        }
    }

}
