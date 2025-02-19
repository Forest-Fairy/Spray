package top.spray.engine.process.infrastructure.factory;

import org.apache.commons.lang3.StringUtils;
import top.spray.core.system.dynamic.SprayClassLoader;
import top.spray.engine.process.processor.dispatch.coordinate.coordinator.SprayProcessCoordinator;
import top.spray.engine.process.processor.dispatch.coordinate.meta.SprayProcessCoordinatorMeta;
import top.spray.engine.process.processor.dispatch.exception.SprayFailedInitExecutorException;
import top.spray.engine.process.processor.execute.step.executor.facade.SprayDefaultStepExecutorFacade;
import top.spray.engine.process.processor.execute.step.executor.facade.SprayStepFacade;
import top.spray.processor.process.step.generator.SprayExecutorGenerator;
import top.spray.engine.process.processor.execute.step.meta.SprayProcessExecuteStepMeta;
import top.spray.processor.util.SprayEngineConfigurations;

public interface SprayStepExecutorFactory {
    Object createExecutor(SprayProcessCoordinator coordinator, SprayStepFacade executorDefinition, boolean tryRemoting);

    static Object create(SprayStepFacade executorDefinition, boolean tryRemoting) {
        SprayProcessCoordinator coordinator = executorDefinition.getCoordinator();
        SprayProcessExecuteStepMeta stepMeta = executorDefinition.getMeta();
        String executorClass = stepMeta.executorClass();
        String executorFactoryClass = stepMeta.executorFactoryClass();
        if (tryRemoting && SprayEngineConfigurations.executorRemotingSupport() &&
                coordinator.getMeta().remoteSupport() && stepMeta.isRemoting()) {
            SprayClassLoader sprayClassLoader = new SprayClassLoader(
                    stepMeta.remotingJarFiles(), coordinator.getClassloader());
            stepExecutor = SprayRemoteAdapterFactory.createRemoteExecutorAdapterForCoordinator(
                    coordinator.getMeta(), stepMeta, sprayClassLoader);
        } else {
            sprayClassLoader = new SprayClassLoader(
                    stepMeta.jarFiles(), coordinator.getClassloader());
            if (StringUtils.isNotBlank(stepMeta.executorFactoryClass())) {
                // create by generator
                stepExecutor = SprayExecutorGenerator.generate(coordinator.getMeta(), stepMeta, sprayClassLoader);
            } else {
                stepExecutor = CreateExecutor(stepMeta.executorClass(), coordinator.getMeta(), stepMeta, sprayClassLoader);
            }
        }
        stepExecutor.initOnlyAtCreate();
        return stepExecutor;
    }




    private static SprayStepFacade CreateExecutor(
            String executorFullClassName, SprayProcessCoordinatorMeta coordinatorMeta,
            SprayProcessExecuteStepMeta executorMeta, SprayClassLoader sprayClassLoader) {
        try {
            Class<?> executorClass = sprayClassLoader.loadClass(executorFullClassName);
            return (SprayDefaultStepExecutorFacade) executorClass.getConstructor().newInstance();
        } catch (Exception e) {
            throw new SprayFailedInitExecutorException(coordinatorMeta, executorMeta, e);
        }
    }
}
