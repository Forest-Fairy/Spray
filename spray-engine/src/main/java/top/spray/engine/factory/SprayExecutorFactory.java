package top.spray.engine.factory;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ReUtil;
import top.spray.core.util.SprayClassLoader;
import top.spray.engine.coordinate.coordinator.SprayProcessCoordinator;
import top.spray.engine.step.executor.SprayProcessStepExecutor;
import top.spray.engine.step.handler.factory.SprayBeforeExecutorCreate;
import top.spray.engine.step.handler.factory.SprayExecutorBeforeCreateHandler;
import top.spray.engine.step.meta.SprayProcessStepMeta;

import java.lang.reflect.InvocationTargetException;

public class SprayExecutorFactory {
    public static String getExecutorId(SprayProcessCoordinator coordinator, SprayProcessStepMeta stepMeta) {
        return coordinator.getMeta().transactionId() + "_" + stepMeta.getId();
    }
    public static SprayProcessStepExecutor create(
            SprayProcessCoordinator coordinator, SprayProcessStepMeta stepMeta) {
        String executorId = getExecutorId(coordinator, stepMeta);
        SprayProcessStepExecutor stepExecutor = coordinator.getStepExecutor(executorId);
        if (stepExecutor == null) {
            synchronized (coordinator) {
                if ((stepExecutor = coordinator.getStepExecutor(stepMeta.getId())) == null) {
                    try {
                        SprayClassLoader sprayClassLoader = new SprayClassLoader(stepMeta.jarFiles());
                        Class<?> executorClass = sprayClassLoader.loadClass(stepMeta.executorClass());
                        SprayBeforeExecutorCreate beforeExecutorCreate = executorClass.getAnnotation(SprayBeforeExecutorCreate.class);
                        if (beforeExecutorCreate != null) {
                            stepExecutor = SprayExecutorBeforeCreateHandler
                                    .get(beforeExecutorCreate.handlerBeforeCreate())
                                    .handle(coordinator, stepMeta);
                        }
                        if (stepExecutor == null) {
                            stepExecutor = (SprayProcessStepExecutor) executorClass.getConstructor().newInstance();
                            stepExecutor.setMeta(stepMeta);
                            stepExecutor.setCoordinator(coordinator);
                            stepExecutor.setClassLoader(sprayClassLoader);
                            stepExecutor.initOnlyAtCreate();
                        }

                        Thread.currentThread().setContextClassLoader(sprayClassLoader);
                    } catch (InstantiationException | IllegalAccessException | NoSuchMethodException |
                             InvocationTargetException | ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                    coordinator.registerExecutor(executorId, stepExecutor);
                }
            }
        }
        return stepExecutor;
    }
}
