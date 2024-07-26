package top.spray.engine.factory;

import top.spray.core.dynamic.loader.SprayClassLoader;
import top.spray.engine.coordinate.meta.SprayProcessCoordinatorMeta;
import top.spray.engine.remoting.SprayRemoteStepExecutor;
import top.spray.engine.remoting.generator.SprayRemoteStepExecutorGenerator;
import top.spray.engine.step.meta.SprayProcessStepMeta;

public class SprayRemoteAdapterFactory {
    private SprayRemoteAdapterFactory() {}

    public static SprayRemoteStepExecutor createRemoteExecutorAdapterForCoordinator(
            SprayProcessCoordinatorMeta coordinatorMeta, SprayProcessStepMeta executorMeta, SprayClassLoader sprayClassLoader) {
        return SprayRemoteStepExecutorGenerator.generate(coordinatorMeta, executorMeta, sprayClassLoader);
    }

//    this method should be realized in adapter realization package
//    public static SprayProcessCoordinatorAdapter createRemoteCoordinatorAdapterForExecutor(
//            SprayProcessCoordinatorMeta coordinatorMeta, SprayProcessStepMeta executorMeta) {
//
//    }

}
