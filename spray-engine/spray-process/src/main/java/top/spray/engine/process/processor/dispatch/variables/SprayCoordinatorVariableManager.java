package top.spray.engine.process.processor.dispatch.variables;

import top.spray.engine.process.infrastructure.prop.SprayVariableContainer;
import top.spray.engine.process.processor.data.event.SprayDataEvent;
import top.spray.engine.process.processor.data.event.impl.SprayDataDispatchResultType;
import top.spray.engine.process.processor.data.manager.SprayVariableManager;
import top.spray.engine.process.processor.dispatch.coordinate.coordinator.SprayProcessCoordinator;
import top.spray.engine.process.processor.execute.step.meta.SprayOptionalData;
import top.spray.engine.process.processor.execute.step.meta.SprayProcessExecuteStepMeta;

public class SprayCoordinatorVariableManager implements SprayVariableManager {
    private final SprayProcessCoordinator coordinator;
    private final SprayCoordinatorVariableContainer coordinatorVariableContainer;

    public SprayCoordinatorVariableManager(SprayProcessCoordinator coordinator) {
        this.coordinator = coordinator;
        this.coordinatorVariableContainer = SprayCoordinatorVariableContainer.create(coordinator);
    }

    @Override
    public SprayProcessCoordinator getCoordinator() {
        return coordinator;
    }

    @Override
    public SprayVariableContainer getProcessVariableContainer() {
        return coordinatorVariableContainer;
    }

    @Override
    public SprayVariableContainer getVariableContainer(String identityDataKey) {
        return null;
    }

    @Override
    public SprayVariableContainer getParentVariableContainer(String identityDataKey) {
        return null;
    }

    @Override
    public SprayVariableContainer easyCopyVariable(String toExecutorNameKey, String variableContainerIdentityDataKey, String fromExecutorNameKey) {
        return null;
    }

    @Override
    public SprayVariableContainer deepCopyVariable(String toExecutorNameKey, String variableContainerIdentityDataKey, String fromExecutorNameKey) {
        return null;
    }

    @Override
    public String setDataDispatchResult(String variableContainerIdentityDataKey, String fromExecutorNameKey, SprayOptionalData optionalData, SprayProcessExecuteStepMeta nextMeta, SprayDataDispatchResultType dataDispatchStatus, Object... params) {
        return "";
    }

    @Override
    public Iterable<String> getInputDataKeys(String executorNameKey) {
        return null;
    }

    @Override
    public Iterable<String> getOutputDataKeys(String executorNameKey) {
        return null;
    }

    @Override
    public Iterable<? extends SprayDataEvent<?>> getDataEvents(String dataKey) {
        return null;
    }
}
