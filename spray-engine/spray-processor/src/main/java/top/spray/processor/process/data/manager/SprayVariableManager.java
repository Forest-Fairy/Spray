package top.spray.processor.process.data.manager;

import top.spray.processor.process.dispatch.coordinate.coordinator.SprayProcessCoordinator;
import top.spray.processor.process.data.event.SprayDataEvent;
import top.spray.processor.process.data.event.impl.SprayDataDispatchResultType;
import top.spray.processor.process.execute.step.meta.SprayOptionalData;
import top.spray.processor.process.execute.step.meta.SprayProcessExecuteStepMeta;
import top.spray.processor.infrustructure.prop.SprayVariableContainer;

/**
 * manager of the executor's variables in the coordinator
 */
public interface SprayVariableManager {
    SprayProcessCoordinator getCoordinator();

    SprayVariableContainer getProcessVariableContainer();

    /** method for getting executor's variableContainer */
    SprayVariableContainer getVariableContainer(String identityDataKey);

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
                                 SprayDataDispatchResultType dataDispatchStatus);

    Iterable<String> getInputDataKeys(String executorNameKey);

    Iterable<String> getOutputDataKeys(String executorNameKey);

    Iterable<? extends SprayDataEvent<?>> getDataEvents(String dataKey);


}
