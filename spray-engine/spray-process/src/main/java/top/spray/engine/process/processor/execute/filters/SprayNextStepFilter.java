package top.spray.engine.process.processor.execute.filters;

import top.spray.engine.process.processor.execute.step.executor.SprayStepExecutor;
import top.spray.engine.process.processor.execute.step.meta.SprayOptionalData;
import top.spray.engine.process.processor.execute.step.meta.SprayProcessExecuteStepMeta;

public interface SprayNextStepFilter {
    /**
     * is next step need to execute with
     * @return false means skip
     */
    boolean filterBeforeDispatch(SprayStepExecutor currentExecutor, String variableContainerIdentityDataKey, SprayOptionalData optionalData, SprayProcessExecuteStepMeta nextStepMeta);
}
