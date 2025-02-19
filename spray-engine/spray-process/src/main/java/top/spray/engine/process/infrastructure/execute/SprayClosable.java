package top.spray.engine.process.infrastructure.execute;

public interface SprayClosable extends AutoCloseable {
    @Override
    default void close() {
        this.shutdown();
    }

    void shutdown();

    void forceShutdown();
}
