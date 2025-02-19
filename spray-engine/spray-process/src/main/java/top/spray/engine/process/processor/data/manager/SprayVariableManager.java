package top.spray.engine.process.processor.data.manager;

import top.spray.engine.process.infrastructure.prop.SprayVariableContainer;
import top.spray.engine.process.processor.dispatch.coordinate.coordinator.SprayProcessCoordinator;
import top.spray.engine.process.processor.data.event.SprayDataEvent;
import top.spray.engine.process.processor.data.event.impl.SprayDataDispatchResultType;
import top.spray.engine.process.processor.execute.step.meta.SprayOptionalData;
import top.spray.engine.process.processor.execute.step.meta.SprayProcessExecuteStepMeta;

/**
 * manager of the executor's variables in the coordinator
 */
public interface SprayVariableManager {
    SprayProcessCoordinator getCoordinator();

    SprayVariableContainer getProcessVariableContainer();

    /** method for getting executor's variableContainer */
    SprayVariableContainer getVariableContainer(String identityDataKey);

    /** method for getting executor's parent variableContainer */
    SprayVariableContainer getParentVariableContainer(String identityDataKey);

    SprayVariableContainer easyCopyVariable(String toExecutorNameKey,
                                            String variableContainerIdentityDataKey,
                                            String fromExecutorNameKey);

    SprayVariableContainer deepCopyVariable(String toExecutorNameKey,
                                            String variableContainerIdentityDataKey,
                                            String fromExecutorNameKey);


    /**
     * set data dispatch result
     * @return dataKey
     */
    String setDataDispatchResult(String variableContainerIdentityDataKey,
                                 String fromExecutorNameKey,
                                 SprayOptionalData optionalData,
                                 SprayProcessExecuteStepMeta nextMeta,
                                 SprayDataDispatchResultType dataDispatchStatus, Object... params);

    Iterable<String> getInputDataKeys(String executorNameKey);

    Iterable<String> getOutputDataKeys(String executorNameKey);

    Iterable<? extends SprayDataEvent<?>> getDataEvents(String dataKey);


}
