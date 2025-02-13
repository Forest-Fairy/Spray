package top.spray.processor.infrustructure.execute;

public interface SprayClosable extends AutoCloseable {
    @Override
    default void close() {
        this.shutdown();
    }

    void shutdown();
    void forceShutdown();
}
