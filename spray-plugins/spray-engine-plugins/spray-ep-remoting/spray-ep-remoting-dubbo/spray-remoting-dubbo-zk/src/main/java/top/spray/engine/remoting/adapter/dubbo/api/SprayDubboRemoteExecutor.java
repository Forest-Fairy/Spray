package top.spray.engine.remoting.adapter.dubbo.api;

import top.spray.core.engine.props.SprayData;
import top.spray.core.thread.SprayPoolExecutor;
import top.spray.core.util.SprayClassLoader;
import top.spray.engine.coordinate.coordinator.SprayProcessCoordinator;
import top.spray.engine.prop.SprayVariableContainer;
import top.spray.engine.remoting.SprayRemoteStepExecutor;
import top.spray.engine.step.executor.SprayProcessStepExecutor;
import top.spray.engine.step.instance.SprayStepResultInstance;
import top.spray.engine.step.meta.SprayProcessStepMeta;

/** this is a executor reference to provider */
public interface SprayDubboRemoteExecutor extends SprayRemoteStepExecutor {
    @Override
    default void initOnlyAtCreate() {

    }

    @Override
    default long getCreateTime() {
        return 0;
    }

    @Override
    default long runningCount() {
        return 0;
    }

    @Override
    default SprayProcessStepMeta getMeta() {
        return null;
    }

    @Override
    default SprayProcessCoordinator getCoordinator() {
        return null;
    }

    @Override
    default SprayClassLoader getClassLoader() {
        return null;
    }

    @Override
    default void setMeta(SprayProcessStepMeta meta) {

    }

    @Override
    default void setCoordinator(SprayProcessCoordinator coordinator) {

    }

    @Override
    default void setClassLoader(SprayClassLoader classLoader) {

    }

    @Override
    default SprayStepResultInstance getStepResult() {
        return null;
    }

    @Override
    default boolean needWait(SprayVariableContainer variables, SprayProcessStepExecutor fromExecutor, SprayData data, boolean still) {
        return false;
    }

    @Override
    default void execute(SprayVariableContainer variables, SprayProcessStepExecutor fromExecutor, SprayData data, boolean still) {

    }

    @Override
    default int varCopyMode() {
        return 0;
    }

    @Override
    default SprayPoolExecutor getThreadPoll() {
        return null;
    }

    @Override
    default void close() throws Exception {

    }
}
