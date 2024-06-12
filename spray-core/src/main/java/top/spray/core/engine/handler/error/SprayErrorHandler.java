package top.spray.core.engine.handler.error;

import top.spray.core.util.ServiceUtil;

import java.util.Map;

public interface SprayErrorHandler<T> {
    boolean canHandle(T t, Throwable throwable, Object args);
    void handle(T t, Throwable error, Object... args);
}
