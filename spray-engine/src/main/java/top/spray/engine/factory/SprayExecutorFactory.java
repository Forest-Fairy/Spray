package top.spray.engine.factory;

import org.apache.commons.lang3.StringUtils;
import top.spray.core.util.SprayClassLoader;
import top.spray.engine.coordinate.coordinator.SprayProcessCoordinator;
import top.spray.engine.exception.SprayExecutorInitError;
import top.spray.engine.step.executor.SprayBaseStepExecutor;
import top.spray.engine.step.executor.SprayProcessStepExecutor;
import top.spray.engine.step.generator.SprayExecutorGenerator;
import top.spray.engine.step.meta.SprayProcessStepMeta;
import top.spray.engine.step.remoting.SprayRemoteStepExecutor;

public class SprayExecutorFactory {
    private SprayExecutorFactory() {}

    public static String getExecutorNameKey(SprayProcessCoordinator coordinator, SprayProcessStepMeta stepMeta) {
        return String.format("%s-%s[%s]", coordinator.getMeta().transactionId(), stepMeta.getName(), stepMeta.getId());
    }
    public static SprayProcessStepExecutor create(
            SprayProcessCoordinator coordinator, SprayProcessStepMeta stepMeta) {
        String executorNameKey = getExecutorNameKey(coordinator, stepMeta);
        SprayProcessStepExecutor stepExecutor = coordinator.getStepExecutor(executorNameKey);
        if (stepExecutor == null) {
            synchronized (coordinator) {
                if ((stepExecutor = coordinator.getStepExecutor(stepMeta.getId())) == null) {
                    SprayClassLoader sprayClassLoader = new SprayClassLoader(stepMeta.jarFiles());
                    if (StringUtils.isNotBlank(stepMeta.executorGeneratorClass())) {
                        // create by generator
                        stepExecutor = SprayExecutorGenerator.generateExecutor(stepMeta.executorGeneratorClass(),
                                coordinator, stepMeta, sprayClassLoader);
                    } else {
                        stepExecutor = CreateExecutor(stepMeta.executorClass(), coordinator, stepMeta, sprayClassLoader);
                    }
                    Thread.currentThread().setContextClassLoader(sprayClassLoader);
                    stepExecutor.setMeta(stepMeta);
                    stepExecutor.setCoordinator(coordinator);
                    stepExecutor.setClassLoader(sprayClassLoader);
                    stepExecutor.initOnlyAtCreate();
                    coordinator.registerExecutor(executorNameKey, stepExecutor);
                }
            }
        }
        return stepExecutor;
    }




    public static SprayProcessStepExecutor CreateExecutor(
            String executorFullClassName, SprayProcessCoordinator coordinator,
            SprayProcessStepMeta executorMeta, SprayClassLoader sprayClassLoader) {
        try {
            Class<?> executorClass = sprayClassLoader.loadClass(executorFullClassName);
            return (SprayBaseStepExecutor) executorClass.getConstructor().newInstance();
        } catch (Exception e) {
            throw SprayExecutorInitError.errorWhenCreateExecutor(coordinator, executorMeta, e);
        }
    }
}
