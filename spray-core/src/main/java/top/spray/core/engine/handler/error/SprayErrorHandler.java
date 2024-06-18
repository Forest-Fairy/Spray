package top.spray.core.engine.handler.error;

public interface SprayErrorHandler<T> {
    boolean canHandle(T t, Throwable throwable, Object[] args);
    void handle(T t, Throwable error, Object... args);
}
