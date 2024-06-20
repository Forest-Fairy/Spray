package top.spray.engine.step.handler.create;

import top.spray.core.util.SprayClassLoader;
import top.spray.engine.coordinate.coordinator.SprayProcessCoordinator;
import top.spray.engine.step.executor.SprayProcessStepExecutor;
import top.spray.engine.step.meta.SprayProcessStepMeta;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public interface SprayExecutorFactory {
    static SprayExecutorFactory get(Class<? extends SprayExecutorFactory> createHandlerClass) {
        return HandlerCacheHolder.get(createHandlerClass);
    }
    static void register(SprayExecutorFactory handler) {
        HandlerCacheHolder.register(handler);
    }
    class HandlerCacheHolder {
        private static final Map<Class<?>, SprayExecutorFactory> HANDLER_CACHE =
                new ConcurrentHashMap<>();
        private static void register(SprayExecutorFactory handler) {
            HANDLER_CACHE.put(handler.getClass(), handler);
        }

        private static SprayExecutorFactory get(Class<? extends SprayExecutorFactory> createHandlerClass) {
            return HANDLER_CACHE.computeIfAbsent(createHandlerClass, key -> {
                try {
                    SprayExecutorFactory handler = createHandlerClass.getConstructor().newInstance();
                    register(handler);
                    return handler;
                } catch (Exception e) {
                    throw new RuntimeException("failed to create handler for " + key.getName(), e);
                }
            });
        }

    }
    static SprayProcessStepExecutor tryCreateExecutor(
            SprayProcessCoordinator coordinator, SprayProcessStepMeta executorMeta,
            Class<?> executorClass, SprayClassLoader sprayClassLoader) {
        SprayExecutorCreationFactory beforeExecutorCreate = executorClass.getAnnotation(SprayExecutorCreationFactory.class);
        if (beforeExecutorCreate != null) {
            return SprayExecutorFactory
                    .get(beforeExecutorCreate.value())
                    .create(coordinator, executorMeta, executorClass, sprayClassLoader);
        } else {
            return null;
        }
    }

    /**
     * handle before step creator being created.
     * @return not null if you don't need to create executor by factory.
     */
    SprayProcessStepExecutor create(SprayProcessCoordinator coordinator, SprayProcessStepMeta executorMeta, Class<?> executorClass, SprayClassLoader sprayClassLoader);
}
