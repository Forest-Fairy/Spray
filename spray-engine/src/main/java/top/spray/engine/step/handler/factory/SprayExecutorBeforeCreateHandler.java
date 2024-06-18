package top.spray.engine.step.handler.factory;

import top.spray.engine.coordinate.coordinator.SprayProcessCoordinator;
import top.spray.engine.step.executor.SprayProcessStepExecutor;
import top.spray.engine.step.meta.SprayProcessStepMeta;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public interface SprayExecutorBeforeCreateHandler {
    static SprayExecutorBeforeCreateHandler get(Class<? extends SprayExecutorBeforeCreateHandler> createHandlerClass) {
        return HandlerCacheHolder.get(createHandlerClass);
    }
    static void register(SprayExecutorBeforeCreateHandler handler) {
        HandlerCacheHolder.register(handler);
    }
    class HandlerCacheHolder {
        private static final Map<Class<?>, SprayExecutorBeforeCreateHandler> HANDLER_CACHE =
                new ConcurrentHashMap<>();
        private static void register(SprayExecutorBeforeCreateHandler handler) {
            HANDLER_CACHE.put(handler.getClass(), handler);
        }

        private static SprayExecutorBeforeCreateHandler get(Class<? extends SprayExecutorBeforeCreateHandler> createHandlerClass) {
            return HANDLER_CACHE.computeIfAbsent(createHandlerClass, key -> {
                try {
                    SprayExecutorBeforeCreateHandler handler = createHandlerClass.getConstructor().newInstance();
                    register(handler);
                    return handler;
                } catch (Exception e) {
                    throw new RuntimeException("failed to create handler for " + key.getName(), e);
                }
            });
        }

    }

    /**
     * handle before step creator being created.
     * @return not null if you don't need to create executor by factory.
     */
    SprayProcessStepExecutor handle(SprayProcessCoordinator coordinator, SprayProcessStepMeta executorMeta);
}
