package top.spray.engine.remoting.adapter.dubbo.api;

import top.spray.core.engine.props.SprayData;
import top.spray.core.engine.status.impl.SprayCoordinateStatus;
import top.spray.core.engine.status.impl.SprayDataDispatchResultStatus;
import top.spray.engine.coordinate.coordinator.SprayProcessCoordinator;
import top.spray.engine.coordinate.meta.SprayProcessCoordinatorMeta;
import top.spray.engine.prop.SprayVariableContainer;
import top.spray.engine.step.condition.SprayNextStepFilter;
import top.spray.engine.step.executor.SprayProcessStepExecutor;
import top.spray.engine.step.meta.SprayProcessStepMeta;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface SprayDubboRemoteCoordinator extends SprayProcessCoordinator {
    @Override
    default SprayProcessCoordinatorMeta getMeta() {
        return null;
    }

    @Override
    default void run() {

    }

    @Override
    default SprayCoordinateStatus status() {
        return null;
    }

    @Override
    default ClassLoader getCreatorThreadClassLoader() {
        return null;
    }

    @Override
    default SprayProcessStepExecutor getStepExecutor(SprayProcessStepMeta executorMeta) {
        return null;
    }

    @Override
    default int createExecutorCount() {
        return 0;
    }

    @Override
    default void dispatch(SprayVariableContainer variables, SprayProcessStepExecutor fromExecutor, SprayNextStepFilter stepFilter, SprayData data, boolean still) {

    }

    @Override
    default List<SprayDataDispatchResultStatus> getDispatchResults(String dataKey) {
        return null;
    }

    @Override
    default void executeNext(SprayProcessStepExecutor nextStepExecutor, SprayVariableContainer lastVariables, SprayProcessStepExecutor fromExecutor, SprayData data, boolean still) {

    }

    @Override
    default Map<String, SprayProcessStepExecutor> getCachedExecutorMap() {
        return null;
    }

    @Override
    default Set<String> getInputDataKeys(String executorNameKey) {
        return null;
    }

    @Override
    default Set<String> getOutputDataKeys(String executorNameKey) {
        return null;
    }

    @Override
    default void close() throws Exception {

    }
}
