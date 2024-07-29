package top.spray.engine.plugins.remote.dubbo.provider.target;

import top.spray.engine.plugins.remote.dubbo.api.target.SprayDubboCoordinator;
import top.spray.engine.plugins.remote.dubbo.api.target.reference.SprayDubboExecutorReference;
import top.spray.engine.plugins.remote.dubbo.util.SprayDubboDataUtil;
import top.spray.engine.step.executor.SprayProcessStepExecutor;


public class SprayDubboExecutorTargetReference implements SprayDubboExecutorReference {
    private final SprayDubboCoordinator dubboCoordinator;
    private final SprayProcessStepExecutor realExecutor;
    public SprayDubboExecutorTargetReference(SprayDubboCoordinator dubboCoordinator, SprayProcessStepExecutor realExecutor) {
        this.dubboCoordinator = dubboCoordinator;
        this.realExecutor = realExecutor;
    }

    @Override
    public boolean needWait(String myNameKey, String variablesIdentityDataKey, String fromExecutorNameKey, byte[] data, boolean still) {
        return realExecutor.needWait(
                dubboCoordinator.getVariablesContainer(variablesIdentityDataKey),
                dubboCoordinator.getStepExecutor(fromExecutorNameKey),
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
