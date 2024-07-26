package top.spray.engine.plugins.remote.dubbo.provider.source;

import top.spray.core.engine.props.SprayData;
import top.spray.engine.prop.SprayVariableContainer;
import top.spray.engine.plugins.remote.dubbo.api.source.SprayDubboExecutor;
import top.spray.engine.plugins.remote.dubbo.api.source.SprayDubboExecutorReference;
import top.spray.engine.step.executor.SprayBaseStepExecutor;
import top.spray.engine.step.executor.SprayProcessStepExecutor;

public class SprayDubboStepExecutor extends SprayBaseStepExecutor implements SprayDubboExecutor {

    private final SprayDubboExecutorReference executorReference;

    public SprayDubboStepExecutor(SprayDubboExecutorReference executorReference) {
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
                data.toJson(), still);
    }


    @Override
    protected void _execute(SprayVariableContainer variables, SprayProcessStepExecutor fromExecutor, SprayData data, boolean still) {
        executorReference.execute(this.getExecutorNameKey(),
                variables.identityDataKey(),
                fromExecutor == null ? null : fromExecutor.getExecutorNameKey(),
                data.toJson(), still);
    }
}
