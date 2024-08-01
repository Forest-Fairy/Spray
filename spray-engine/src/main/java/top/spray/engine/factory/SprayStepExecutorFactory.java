package top.spray.engine.factory;

import org.apache.commons.lang3.StringUtils;
import top.spray.core.dynamic.loader.SprayClassLoader;
import top.spray.engine.coordinate.coordinator.SprayProcessCoordinator;
import top.spray.engine.coordinate.meta.SprayProcessCoordinatorMeta;
import top.spray.engine.exception.SprayExecutorInitException;
import top.spray.engine.step.executor.SprayDefaultStepExecutorDefinition;
import top.spray.engine.step.executor.SprayExecutorDefinition;
import top.spray.engine.step.generator.SprayExecutorGenerator;
import top.spray.engine.step.meta.SprayProcessStepMeta;
import top.spray.engine.util.SprayEngineConfigurations;

public interface SprayStepExecutorFactory {
    Object createExecutor(SprayProcessCoordinator coordinator, SprayExecutorDefinition executorDefinition, boolean tryRemoting);

    static Object create(SprayExecutorDefinition executorDefinition, boolean tryRemoting) {
        SprayProcessCoordinator coordinator = executorDefinition.getCoordinator();
        SprayProcessStepMeta stepMeta = executorDefinition.getMeta();
        String executorClass = stepMeta.executorClass();
        String executorFactoryClass = stepMeta.executorFactoryClass();
        if (tryRemoting && SprayEngineConfigurations.executorRemotingSupport() &&
                coordinator.getMeta().remoteSupport() && stepMeta.isRemoting()) {
            SprayClassLoader sprayClassLoader = new SprayClassLoader(
                    stepMeta.remotingJarFiles(), coordinator.getCreatorThreadClassLoader());
            stepExecutor = SprayRemoteAdapterFactory.createRemoteExecutorAdapterForCoordinator(
                    coordinator.getMeta(), stepMeta, sprayClassLoader);
        } else {
            sprayClassLoader = new SprayClassLoader(
                    stepMeta.jarFiles(), coordinator.getCreatorThreadClassLoader());
            if (StringUtils.isNotBlank(stepMeta.executorFactoryClass())) {
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




    private static SprayExecutorDefinition CreateExecutor(
            String executorFullClassName, SprayProcessCoordinatorMeta coordinatorMeta,
            SprayProcessStepMeta executorMeta, SprayClassLoader sprayClassLoader) {
        try {
            Class<?> executorClass = sprayClassLoader.loadClass(executorFullClassName);
            return (SprayDefaultStepExecutorDefinition) executorClass.getConstructor().newInstance();
        } catch (Exception e) {
            throw new SprayExecutorInitException(coordinatorMeta, executorMeta, e);
        }
    }
}
