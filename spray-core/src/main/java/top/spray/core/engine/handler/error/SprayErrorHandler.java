package top.spray.core.engine.handler.error;

import top.spray.core.util.ServiceUtil;

import java.util.Map;

public interface SprayErrorHandler<T> {
    boolean canHandle(T t, Throwable throwable, Object args);
    void handle(T t, Throwable error, Object... args);

    class InstanceLoader {
        private static Map<String, SprayErrorHandler> INSTANCE_MAP = null;

        private static synchronized void init() {
            if (INSTANCE_MAP == null) {
                INSTANCE_MAP = ServiceUtil.loadServiceClassNameMap(SprayErrorHandler.class);
            }
        }

        private static SprayErrorHandler<?> get(String handlerName) {
            init();
            return INSTANCE_MAP.get(handlerName);
        }
        private static <T> SprayErrorHandler<T> get(Class<? extends SprayErrorHandler<T>> handlerClass) {
            return (SprayErrorHandler<T>) INSTANCE_MAP.get(handlerClass.getName());
        }
        private static void register(SprayErrorHandler<?> handler) {
            init();
            INSTANCE_MAP.put(handler.getClass().getName(), handler);
        }
    }
    static SprayErrorHandler<?> getInstance(String errorHandlerTypeName) {
        return InstanceLoader.get(errorHandlerTypeName);
    }
    static <T> SprayErrorHandler<T> getInstance(Class<? extends SprayErrorHandler<T>> handlerClass) {
        return InstanceLoader.get(handlerClass);
    }


}
