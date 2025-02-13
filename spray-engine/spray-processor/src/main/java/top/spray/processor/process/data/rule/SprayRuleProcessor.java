package top.spray.processor.process.data.rule;

import top.spray.processor.infrustructure.prop.SprayVariableContainer;
import top.spray.processor.process.execute.step.meta.SprayOptionalData;

public interface SprayRuleProcessor {
    void process(SprayRule rule, SprayOptionalData optionalData, SprayVariableContainer variableContainer) throws Exception;
    void whenError(SprayRule rule, SprayOptionalData optionalData, SprayVariableContainer variableContainer, Exception exception);
}
