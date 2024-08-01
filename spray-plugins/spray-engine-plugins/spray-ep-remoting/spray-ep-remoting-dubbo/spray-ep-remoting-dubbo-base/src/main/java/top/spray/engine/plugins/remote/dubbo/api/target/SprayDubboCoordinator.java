package top.spray.engine.plugins.remote.dubbo.api.target;

import top.spray.engine.coordinate.coordinator.SprayProcessCoordinator;
import top.spray.engine.plugins.remote.dubbo.api.target.holder.SprayDubboCoordinatorReferenceHolder;
import top.spray.engine.step.executor.SprayExecutorDefinition;

/** this is a coordinator reference */
public interface SprayDubboCoordinator extends
        SprayProcessCoordinator,
        SprayDubboCoordinatorReferenceHolder {
    String url();
    String protocol();
    void registerExecutor(String executorNameKey, SprayExecutorDefinition executor);
}
