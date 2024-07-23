package top.spray.engine.remoting.adapter.dubbo.consumer.generator;

import top.spray.core.engine.props.SprayData;
import top.spray.core.thread.SprayPoolExecutor;
import top.spray.core.util.SprayClassLoader;
import top.spray.engine.coordinate.coordinator.SprayProcessCoordinator;
import top.spray.engine.prop.SprayVariableContainer;
import top.spray.engine.step.adapter.SprayStepExecutorAdapter;
import top.spray.engine.step.executor.SprayProcessStepExecutor;
import top.spray.engine.step.instance.SprayStepResultInstance;
import top.spray.engine.step.meta.SprayProcessStepMeta;

public final class SprayStepExecutorDubboAdapter implements SprayStepExecutorAdapter {
    @Override
    public boolean initOnlyAtCreate() {

    }

    @Override
    public long getCreateTime() {
        return 0;
    }

    @Override
    public long runningCount() {
        return 0;
    }

    @Override
    public SprayProcessStepMeta getMeta() {
        return null;
    }

    @Override
    public SprayProcessCoordinator getCoordinator() {
        return null;
    }

    @Override
    public SprayClassLoader getClassLoader() {
        return null;
    }

    @Override
    public void setMeta(SprayProcessStepMeta meta) {

    }

    @Override
    public void setCoordinator(SprayProcessCoordinator coordinator) {

    }

    @Override
    public void setClassLoader(SprayClassLoader classLoader) {

    }

    @Override
    public SprayStepResultInstance getStepResult() {
        return null;
    }

    @Override
    public boolean needWait(SprayVariableContainer variables, SprayProcessStepExecutor fromExecutor, SprayData data, boolean still) {
        return false;
    }

    @Override
    public void execute(SprayVariableContainer variables, SprayProcessStepExecutor fromExecutor, SprayData data, boolean still) {

    }

    @Override
    public int varCopyMode() {
        return 0;
    }

    @Override
    public SprayPoolExecutor getThreadPoll() {
        return null;
    }

    @Override
    public void close() throws Exception {

    }
}
