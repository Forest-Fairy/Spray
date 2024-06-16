package top.spray.engine.factory;

import top.spray.core.util.SprayClassLoader;
import top.spray.engine.coordinate.coordinator.SprayProcessCoordinator;
import top.spray.engine.step.executor.SprayProcessStepExecutor;
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
                        stepExecutor = (SprayProcessStepExecutor) sprayClassLoader
                                .loadClass(stepMeta.executorClass()).getConstructor().newInstance();
                        stepExecutor.setMeta(stepMeta);
                        stepExecutor.setCoordinator(coordinator);
                        stepExecutor.setClassLoader(sprayClassLoader);
                        Thread.currentThread().setContextClassLoader(sprayClassLoader);
                        stepExecutor.initOnlyAtCreate();
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
