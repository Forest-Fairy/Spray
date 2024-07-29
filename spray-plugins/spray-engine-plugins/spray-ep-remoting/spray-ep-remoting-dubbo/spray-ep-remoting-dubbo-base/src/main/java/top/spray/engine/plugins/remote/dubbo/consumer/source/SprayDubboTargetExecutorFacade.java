package top.spray.engine.plugins.remote.dubbo.consumer.source;

import top.spray.core.engine.props.SprayData;
import top.spray.engine.plugins.remote.dubbo.api.source.holder.SprayDubboExecutorReferenceHolder;
import top.spray.engine.plugins.remote.dubbo.util.SprayDubboDataUtil;
import top.spray.engine.prop.SprayVariableContainer;
import top.spray.engine.plugins.remote.dubbo.api.source.SprayDubboExecutor;
import top.spray.engine.plugins.remote.dubbo.api.target.reference.SprayDubboExecutorReference;
import top.spray.engine.step.executor.SprayBaseStepExecutor;
import top.spray.engine.step.executor.SprayProcessStepExecutor;

public class SprayDubboTargetExecutorFacade extends SprayBaseStepExecutor implements SprayDubboExecutor, SprayDubboExecutorReferenceHolder {

    private final SprayDubboExecutorReference executorReference;

    public SprayDubboTargetExecutorFacade(SprayDubboExecutorReference executorReference) {
        this.executorReference = executorReference;
    }

    @Override
    public SprayDubboExecutorReference getExecutorReference() {
        return executorReference;
    }

    @Override
    protected boolean needWait0(SprayVariableContainer variables, SprayProcessStepExecutor fromExecutor, SprayData data, boolean still) {
        return executorReference.needWait(this.getExecutorNameKey(),
                variables.identityDataKey(),
                fromExecutor == null ? null : fromExecutor.getExecutorNameKey(),
                SprayDubboDataUtil.wrapData(this.getCoordinator().getMeta(), this.getMeta(), data),
                still);
    }



    @Override
    protected void _execute(SprayVariableContainer variables, SprayProcessStepExecutor fromExecutor, SprayData data, boolean still) {
        executorReference.execute(this.getExecutorNameKey(),
                variables.identityDataKey(),
                fromExecutor == null ? null : fromExecutor.getExecutorNameKey(),
                SprayDubboDataUtil.wrapData(this.getCoordinator().getMeta(), this.getMeta(), data),
                still);
    }

    @Override
    public void closeInRuntime() {

    }
}
