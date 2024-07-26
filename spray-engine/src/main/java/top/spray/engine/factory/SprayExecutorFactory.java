package top.spray.engine.factory;

import org.apache.commons.lang3.StringUtils;
import top.spray.core.dynamic.loader.SprayClassLoader;
import top.spray.engine.coordinate.coordinator.SprayProcessCoordinator;
import top.spray.engine.coordinate.meta.SprayProcessCoordinatorMeta;
import top.spray.engine.exception.SprayExecutorInitException;
import top.spray.engine.step.executor.SprayBaseStepExecutor;
import top.spray.engine.step.executor.SprayProcessStepExecutor;
import top.spray.engine.step.generator.SprayExecutorGenerator;
import top.spray.engine.step.meta.SprayProcessStepMeta;
import top.spray.engine.util.SprayEngineConfigurations;

public class SprayExecutorFactory {
    private SprayExecutorFactory() {}

    public static String getExecutorNameKey(SprayProcessCoordinatorMeta coordinatorMeta, SprayProcessStepMeta stepMeta) {
        return String.format("%s-%s[%s]", coordinatorMeta.transactionId(), stepMeta.getName(), stepMeta.getId());
    }
    public static SprayProcessStepExecutor create(
            SprayProcessCoordinator coordinator, SprayProcessStepMeta stepMeta, boolean tryRemoting) {
        SprayProcessStepExecutor stepExecutor;
        SprayClassLoader sprayClassLoader;
        if (tryRemoting && SprayEngineConfigurations.executorRemotingSupport() && stepMeta.isRemoting()) {
            sprayClassLoader = new SprayClassLoader(
                    stepMeta.remotingJarFiles(), coordinator.getCreatorThreadClassLoader());
            stepExecutor = SprayRemoteAdapterFactory.createRemoteExecutorAdapterForCoordinator(
                    coordinator.getMeta(), stepMeta, sprayClassLoader);
        } else {
            sprayClassLoader = new SprayClassLoader(
                    stepMeta.jarFiles(), coordinator.getCreatorThreadClassLoader());
            if (StringUtils.isNotBlank(stepMeta.executorGeneratorClass())) {
                // create by generator
                stepExecutor = SprayExecutorGenerator.generate(coordinator.getMeta(), stepMeta, sprayClassLoader);
            } else {
                stepExecutor = CreateExecutor(stepMeta.executorClass(), coordinator.getMeta(), stepMeta, sprayClassLoader);
            }
        }
        stepExecutor.setMeta(stepMeta);
        stepExecutor.setCoordinator(coordinator);
        stepExecutor.setClassLoader(sprayClassLoader);
        stepExecutor.initOnlyAtCreate();
        return stepExecutor;
    }




    private static SprayProcessStepExecutor CreateExecutor(
            String executorFullClassName, SprayProcessCoordinatorMeta coordinatorMeta,
            SprayProcessStepMeta executorMeta, SprayClassLoader sprayClassLoader) {
        try {
            Class<?> executorClass = sprayClassLoader.loadClass(executorFullClassName);
            return (SprayBaseStepExecutor) executorClass.getConstructor().newInstance();
        } catch (Exception e) {
            throw new SprayExecutorInitException(coordinatorMeta, executorMeta, e);
        }
    }
}
