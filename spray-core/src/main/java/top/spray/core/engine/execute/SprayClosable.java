package top.spray.core.engine.execute;

public interface SprayClosable extends AutoCloseable {
    @Override
    default void close() throws Exception {
        this.closeInRuntime();
    }

    void closeInRuntime();
}
