package top.spray.core.stream.iterator;

import top.spray.common.data.SprayData;
import top.spray.common.tools.SprayOptional;
import top.spray.core.stream.SprayDataIterator;

public class SprayDataEmptyBlockingIterator extends SprayDataIterator {

    private final long blockTimeout;
    private final boolean voidResult;

    public SprayDataEmptyBlockingIterator(SprayDataIterator wrappedIterator, long blockTimeout, boolean voidResult) {
        super(wrappedIterator);
        this.blockTimeout = blockTimeout;
        this.voidResult = voidResult;
    }


    @Override
    protected SprayOptional<SprayData> overwriteNext(SprayDataIterator next) {
        long deadline = System.currentTimeMillis() + blockTimeout;
        while (! self().hasNext()) {
            if (System.currentTimeMillis() > deadline) {
                break;
            }
            yieldAndCheckIfInterrupted();
        }
        if (System.currentTimeMillis() > deadline) {
            if (voidResult) {
                return SprayOptional.empty();
            } else {
                throw new RuntimeException("blocking timeout");
            }
        }
        return next.next();
    }
}
