package top.spray.engine.process.processor.execute.step.executor.facade;

import top.spray.engine.process.infrastructure.prop.SprayVariableContainer;
import top.spray.engine.process.processor.execute.step.executor.SprayStepExecutor;
import top.spray.engine.process.processor.execute.step.meta.SprayOptionalData;
import top.spray.engine.process.processor.execute.step.meta.SprayProcessExecuteStepMeta;

import java.util.List;

public interface SprayStepExecutorOwner {
    String getExecutorNameKey();

    SprayProcessExecuteStepMeta getStepMeta();

    List<? extends SprayProcessExecuteStepMeta> listNextSteps();

    String getExecutorNameKey(SprayProcessExecuteStepMeta nextStepMeta);

    void dispatchData(String variableContainerIdentityDataKey, SprayStepExecutor sprayStepExecutor, SprayOptionalData optionalData, String toExecutorNameKeys);

    SprayStepFacade getExecutorFacade(String executorNameKey);

    SprayVariableContainer getVariableContainer(String variableContainerIdentityDataKey);
}
