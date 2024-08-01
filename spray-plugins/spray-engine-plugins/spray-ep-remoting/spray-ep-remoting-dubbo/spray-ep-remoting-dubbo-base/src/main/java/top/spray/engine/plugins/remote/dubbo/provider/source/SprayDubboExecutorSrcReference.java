package top.spray.engine.plugins.remote.dubbo.provider.source;

import top.spray.engine.coordinate.coordinator.SprayProcessCoordinator;
import top.spray.engine.plugins.remote.dubbo.api.target.reference.SprayDubboExecutorReference;
import top.spray.engine.plugins.remote.dubbo.util.SprayDubboDataUtil;
import top.spray.engine.step.executor.SprayExecutorDefinition;


public class SprayDubboExecutorSrcReference implements SprayDubboExecutorReference {
    private final SprayProcessCoordinator coordinator;
    private final SprayExecutorDefinition realExecutor;
    public SprayDubboExecutorSrcReference(SprayProcessCoordinator coordinator, SprayExecutorDefinition realExecutor) {
        this.coordinator = coordinator;
        this.realExecutor = realExecutor;
    }

    @Override
    public boolean needWait(String myNameKey, String variablesIdentityDataKey, String fromExecutorNameKey, byte[] data, boolean still) {
        return realExecutor.needWait(
                coordinator.getVariablesContainer(variablesIdentityDataKey),
                coordinator.getStepExecutor(fromExecutorNameKey),
                SprayDubboDataUtil.resolveData(dubboCoordinator.getMeta(), realExecutor.getMeta(), data),
                still);
    }

    @Override
    public void execute(String myNameKey, String variablesIdentityDataKey, String fromExecutorNameKey, byte[] data, boolean still) {
        realExecutor.execute(
                dubboCoordinator.getVariablesContainer(variablesIdentityDataKey),
                dubboCoordinator.getStepExecutor(fromExecutorNameKey),
                SprayDubboDataUtil.resolveData(dubboCoordinator.getMeta(), realExecutor.getMeta(), data),
                still);
    }

}
