package top.spray.core.base.stream;

import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public interface SprayDataPipeLine<Pipeline extends SprayDataPipeLine<?>> extends AutoCloseable {

    @NotNull SprayDataIterator cachedIterator(String key, Function<Pipeline, SprayDataIterator> iteratorSupplier);
    @NotNull SprayDataIterator notCachedIterator(Function<Pipeline, SprayDataIterator> iteratorSupplier);

    String getProperty(String key);

    long curLength() ;

    /** true if the pipeline is no more increment */
    boolean isDone();

    /** true if the pipeline is unreadable */
    boolean isClosed() ;

    /** true if the pipeline is readable */
    boolean isOpen() ;

}
