package top.spray.engine.process.infrastructure.factory;

import top.spray.core.system.dynamic.SprayClassLoader;
import top.spray.engine.process.processor.dispatch.coordinate.meta.SprayProcessCoordinatorMeta;
import top.spray.processor.process.slots.remoting.SprayRemoteStepExecutorFacade;
import top.spray.processor.process.slots.remoting.generator.SprayRemoteStepExecutorGenerator;
import top.spray.engine.process.processor.execute.step.meta.SprayProcessExecuteStepMeta;

public class SprayRemoteAdapterFactory {
    private SprayRemoteAdapterFactory() {}

    public static SprayRemoteStepExecutorFacade createRemoteExecutorAdapterForCoordinator(
            SprayProcessCoordinatorMeta coordinatorMeta, SprayProcessExecuteStepMeta executorMeta, SprayClassLoader sprayClassLoader) {
        return SprayRemoteStepExecutorGenerator.generate(coordinatorMeta, executorMeta, sprayClassLoader);
    }

//    this method should be realized in adapter realization package
//    public static SprayProcessCoordinatorAdapter createRemoteCoordinatorAdapterForExecutor(
//            SprayProcessCoordinatorMeta coordinatorMeta, SprayProcessStepMeta executorMeta) {
//
//    }

}
