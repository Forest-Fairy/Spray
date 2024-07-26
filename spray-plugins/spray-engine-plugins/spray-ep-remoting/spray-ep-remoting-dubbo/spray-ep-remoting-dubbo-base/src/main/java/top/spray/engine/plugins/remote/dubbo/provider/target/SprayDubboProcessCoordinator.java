package top.spray.engine.plugins.remote.dubbo.provider.target;

import top.spray.core.engine.props.SprayData;
import top.spray.core.engine.types.coordinate.status.SprayCoordinatorStatus;
import top.spray.core.engine.types.data.dispatch.result.SprayDataDispatchResultStatus;
import top.spray.engine.coordinate.meta.SprayProcessCoordinatorMeta;
import top.spray.engine.prop.SprayVariableContainer;
import top.spray.engine.plugins.remote.dubbo.api.target.SprayDubboCoordinator;
import top.spray.engine.plugins.remote.dubbo.api.target.reference.SprayDubboCoordinatorReference;
import top.spray.engine.step.condition.SprayNextStepFilter;
import top.spray.engine.step.executor.SprayProcessStepExecutor;
import top.spray.engine.step.meta.SprayProcessStepMeta;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class SprayDubboProcessCoordinator implements SprayDubboCoordinator {

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
        return coordinatorMeta;
    }

    @Override
    public SprayCoordinatorStatus get() {
        throw new SprayDubboOperationNotSupportException();
    }

    @Override
    public void run() {
        throw new SprayDubboOperationNotSupportException();
    }

    @Override
    public SprayCoordinatorStatus status() {
        return SprayCoordinatorStatus.get(coordinatorReference.getCoordinatorStatus(this.coordinatorMeta.transactionId()));
    }

    @Override
    public String getExecutorNameKey(SprayProcessStepExecutor executor) {
        return SprayDubboCoordinator.super.getExecutorNameKey(executor);
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
    public void dispatch(SprayVariableContainer variables, SprayNextStepFilter stepFilter, SprayProcessStepExecutor fromExecutor, SprayData data, boolean still) {

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
