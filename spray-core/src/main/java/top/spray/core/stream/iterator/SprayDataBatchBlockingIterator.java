package top.spray.core.stream.iterator;

import top.spray.common.data.SprayData;
import top.spray.common.tools.SprayOptional;
import top.spray.core.stream.SprayDataIterator;

import java.util.concurrent.atomic.LongAdder;

public class SprayDataBatchBlockingIterator extends SprayDataIterator {

    private final long blockTimeout;
    private final boolean voidResult;
    private final int batchSize;
    private final LongAdder counter;

    public SprayDataBatchBlockingIterator(SprayDataIterator wrappedIterator, int batchSize, long blockTimeout, boolean voidResult) {
        super(wrappedIterator);
        this.blockTimeout = blockTimeout;
        this.voidResult = voidResult;
        this.batchSize = batchSize;
        this.counter = new LongAdder();
    }


    public long batchNo() {
        return counter.longValue() / batchSize;
    }

    public long batchSize() {
        return batchSize;
    }

    @Override
    protected SprayOptional<SprayData> overwriteNext(SprayDataIterator next) {
        long curTime = System.currentTimeMillis();
        long curCount = counter.longValue();
        long distance = getDataStream().curLength() - curCount;
        while (distance < batchSize) {
            if (System.currentTimeMillis() - curTime > blockTimeout) {
                if (voidResult) {
                    return SprayOptional.empty();
                } else {
                    throw new RuntimeException("blocking timeout");
                }
            }
            distance = getDataStream().curLength() - curCount;
            yieldAndCheckIfInterrupted();
        }
        counter.increment();
        return next.next();
    }
}
