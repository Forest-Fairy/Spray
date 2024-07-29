package top.spray.engine.plugins.remote.dubbo.consumer.target;

import top.spray.core.dynamic.loader.SprayClassLoader;
import top.spray.core.engine.props.SprayData;
import top.spray.core.thread.SprayPoolExecutor;
import top.spray.engine.coordinate.coordinator.SprayProcessCoordinator;
import top.spray.engine.plugins.remote.dubbo.api.source.SprayDubboExecutor;
import top.spray.engine.plugins.remote.dubbo.api.source.holder.SprayDubboExecutorReferenceHolder;
import top.spray.engine.plugins.remote.dubbo.api.target.reference.SprayDubboExecutorReference;
import top.spray.engine.plugins.remote.dubbo.provider.source.SprayDubboExecutorSrcReference;
import top.spray.engine.prop.SprayVariableContainer;
import top.spray.engine.step.executor.SprayProcessStepExecutor;
import top.spray.engine.step.instance.SprayStepResultInstance;
import top.spray.engine.step.meta.SprayProcessStepMeta;

public class SprayDubboSrcExecutorFacade implements SprayDubboExecutor, SprayDubboExecutorReferenceHolder {

    private final SprayDubboExecutorSrcReference executorReference;

    public SprayDubboSrcExecutorFacade(SprayDubboExecutorSrcReference executorReference) {
        this.executorReference = executorReference;
    }

    @Override
    public SprayDubboExecutorReference getExecutorReference() {
        return executorReference;
    }

    @Override
    public boolean initOnlyAtCreate() {
        return false;
    }

    @Override
    public long getCreateTime() {
        return this.realExecutor.getCreateTime();
    }

    @Override
    public long runningCount() {
        return this.realExecutor.runningCount();
    }

    @Override
    public SprayProcessStepMeta getMeta() {
        return this.realExecutor.getMeta();
    }

    @Override
    public SprayProcessCoordinator getCoordinator() {
        return this.realExecutor.getCoordinator();
    }

    @Override
    public SprayClassLoader getClassLoader() {
        return this.realExecutor.getClassLoader();
    }

    @Override
    public void setMeta(SprayProcessStepMeta meta) {
        this.realExecutor.setMeta(meta);
    }

    @Override
    public void setCoordinator(SprayProcessCoordinator coordinator) {
        this.realExecutor.setCoordinator(coordinator);
    }

    @Override
    public void setClassLoader(SprayClassLoader classLoader) {


    }

    @Override
    public SprayStepResultInstance getStepResult() {
        return this.realExecutor.getStepResult();
    }

    @Override
    public boolean needWait(SprayVariableContainer variables, SprayProcessStepExecutor fromExecutor, SprayData data, boolean still) {
        return this.realExecutor.needWait(variables, fromExecutor, data, still);
    }

    @Override
    public void execute(SprayVariableContainer variables, SprayProcessStepExecutor fromExecutor, SprayData data, boolean still) {
        this.realExecutor.execute(variables, fromExecutor, data, still);
    }

    @Override
    public int varCopyMode() {
        return this.realExecutor.varCopyMode();
    }

    @Override
    public SprayPoolExecutor getThreadPoll() {
        return this.realExecutor.getThreadPoll();
    }


    @Override
    public void closeInRuntime() {

    }

}
