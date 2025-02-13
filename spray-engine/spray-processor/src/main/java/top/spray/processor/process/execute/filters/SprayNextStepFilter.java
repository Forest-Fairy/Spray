package top.spray.processor.process.execute.filters;

import top.spray.processor.process.execute.step.executor.SprayStepExecutor;
import top.spray.processor.process.execute.step.meta.SprayOptionalData;
import top.spray.processor.process.execute.step.meta.SprayProcessExecuteStepMeta;

public interface SprayNextStepFilter {
    /**
     * is next step need to execute with
     * @return false means skip
     */
    boolean filterBeforeDispatch(SprayStepExecutor currentExecutor, String variableContainerIdentityDataKey, SprayOptionalData optionalData, SprayProcessExecuteStepMeta nextStepMeta);
}
