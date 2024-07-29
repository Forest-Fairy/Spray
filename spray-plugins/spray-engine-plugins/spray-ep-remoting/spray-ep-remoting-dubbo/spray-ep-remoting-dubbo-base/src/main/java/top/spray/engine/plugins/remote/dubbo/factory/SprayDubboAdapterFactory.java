package top.spray.engine.plugins.remote.dubbo.factory;

import top.spray.core.engine.props.SprayData;
import top.spray.engine.factory.SprayExecutorFactory;
import top.spray.engine.plugins.remote.dubbo.api.target.SprayDubboCoordinator;
import top.spray.engine.plugins.remote.dubbo.api.target.reference.SprayDubboExecutorReference;
import top.spray.engine.step.executor.SprayProcessStepExecutor;
import top.spray.engine.step.meta.SprayProcessStepMeta;

public class SprayDubboAdapterFactory {
    public static void generateExecutor(SprayDubboCoordinator coordinator,
                                           String executorNameKey, String executorMeta) {
        SprayProcessStepMeta stepMeta = new SprayProcessStepMeta(SprayData.fromJson(executorMeta));
        SprayProcessStepExecutor realExecutor = SprayExecutorFactory.create(
                coordinator, stepMeta, false);
        coordinator.registerExecutor(executorNameKey, realExecutor);
        SprayDubboExecutorReference.createTargetProvider(executorNameKey, coordinator, realExecutor);
    }
}
