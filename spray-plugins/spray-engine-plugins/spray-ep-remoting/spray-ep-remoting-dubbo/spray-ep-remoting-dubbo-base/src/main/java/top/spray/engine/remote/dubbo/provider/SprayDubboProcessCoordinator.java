package top.spray.engine.remote.dubbo.provider;

import top.spray.core.engine.props.SprayData;
import top.spray.core.engine.status.impl.SprayCoordinateStatus;
import top.spray.core.engine.status.impl.SprayDataDispatchResultStatus;
import top.spray.engine.coordinate.coordinator.SprayDefaultProcessCoordinator;
import top.spray.engine.coordinate.meta.SprayProcessCoordinatorMeta;
import top.spray.engine.prop.SprayVariableContainer;
import top.spray.engine.remote.dubbo.api.SprayDubboCoordinator;
import top.spray.engine.remote.dubbo.api.SprayDubboCoordinatorReference;
import top.spray.engine.step.condition.SprayNextStepFilter;
import top.spray.engine.step.executor.SprayProcessStepExecutor;
import top.spray.engine.step.meta.SprayProcessStepMeta;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class SprayDubboProcessCoordinator extends SprayDefaultProcessCoordinator implements SprayDubboCoordinator {

    private SprayDubboCoordinatorReference coordinatorReference;
    private SprayProcessCoordinatorMeta coordinatorMeta;

    public SprayDubboProcessCoordinator(
            SprayDubboCoordinatorReference coordinatorReference,
            SprayProcessCoordinatorMeta coordinatorMeta) {
        this.coordinatorMeta = coordinatorMeta;
    }

    @Override
    public SprayDubboCoordinatorReference getCoordinatorReference() {
        return coordinatorReference;
    }

    @Override
    public SprayProcessCoordinatorMeta getMeta() {
        return null;
    }

    @Override
    public void run() {
        // TODO return specific error
        throw new IllegalArgumentException("this coordinator can not be run");
    }

    @Override
    public SprayCoordinateStatus status() {
        return null;
    }

    @Override
    public ClassLoader getCreatorThreadClassLoader() {
        return null;
    }

    @Override
    public SprayProcessStepExecutor getStepExecutor(SprayProcessStepMeta executorMeta) {
        return null;
    }

    @Override
    public int createExecutorCount() {
        return 0;
    }

    @Override
    public void dispatch(SprayVariableContainer variables, SprayProcessStepExecutor fromExecutor, SprayNextStepFilter stepFilter, SprayData data, boolean still) {

    }

    @Override
    public List<SprayDataDispatchResultStatus> getDispatchResults(String dataKey) {
        return null;
    }

    @Override
    public void executeNext(SprayProcessStepExecutor nextStepExecutor, SprayVariableContainer lastVariables, SprayProcessStepExecutor fromExecutor, SprayData data, boolean still) {

    }

    @Override
    public Map<String, SprayProcessStepExecutor> getCachedExecutorMap() {
        return null;
    }

    @Override
    public Set<String> getInputDataKeys(String executorNameKey) {
        return null;
    }

    @Override
    public Set<String> getOutputDataKeys(String executorNameKey) {
        return null;
    }

    @Override
    public void close() throws Exception {

    }
}
