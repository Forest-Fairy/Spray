package top.spray.core.engine.factory;

import top.spray.core.engine.handler.error.SprayErrorHandler;
import top.spray.core.util.ServiceUtil;

import java.util.Map;

public class SprayErrorHandlerFactory {
    private static final Map<String, SprayErrorHandler> INSTANCE_MAP = ServiceUtil.loadServiceClassNameMap(SprayErrorHandler.class);
    public static SprayErrorHandler<?> getInstance(String handlerName) {
        return INSTANCE_MAP.get(handlerName);
    }
    public static <T> SprayErrorHandler<T> getInstance(Class<? extends SprayErrorHandler<T>> handlerClass) {
        return (SprayErrorHandler<T>) INSTANCE_MAP.get(handlerClass.getName());
    }
    public static void register(SprayErrorHandler<?> handler) {
        INSTANCE_MAP.put(handler.getClass().getName(), handler);
    }
}
