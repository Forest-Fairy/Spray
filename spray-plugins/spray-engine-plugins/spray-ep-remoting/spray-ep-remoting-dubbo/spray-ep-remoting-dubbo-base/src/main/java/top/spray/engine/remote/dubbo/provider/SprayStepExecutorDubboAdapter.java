package top.spray.engine.remote.dubbo.provider;

import top.spray.core.engine.props.SprayData;
import top.spray.core.thread.SprayPoolExecutor;
import top.spray.engine.prop.SprayVariableContainer;
import top.spray.engine.remoting.SprayRemoteStepExecutor;
import top.spray.engine.step.executor.SprayBaseStepExecutor;
import top.spray.engine.step.executor.SprayProcessStepExecutor;

public final class SprayStepExecutorDubboAdapter extends SprayBaseStepExecutor implements SprayRemoteStepExecutor {
    @Override
    protected void initOnlyAtCreate0() {
        super.initOnlyAtCreate0();
    }

    @Override
    protected boolean needWait0(SprayVariableContainer variables, SprayProcessStepExecutor fromExecutor, SprayData data, boolean still) {
        return super.needWait0(variables, fromExecutor, data, still);
    }

    @Override
    public int varCopyMode() {
        return super.varCopyMode();
    }

    @Override
    public SprayPoolExecutor getThreadPoll() {
        return super.getThreadPoll();
    }

    @Override
    protected void destroy() throws Exception {
        super.destroy();
    }

    @Override
    protected void _execute(SprayVariableContainer variables, SprayProcessStepExecutor fromExecutor, SprayData data, boolean still) {

    }
}
