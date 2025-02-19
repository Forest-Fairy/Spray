package top.spray.engine.process.processor.data.rule;

import top.spray.engine.process.infrastructure.prop.SprayVariableContainer;
import top.spray.engine.process.processor.execute.step.meta.SprayOptionalData;

public interface SprayRuleProcessor {
    void process(SprayRule rule, SprayOptionalData optionalData, SprayVariableContainer variableContainer) throws Exception;
    void whenError(SprayRule rule, SprayOptionalData optionalData, SprayVariableContainer variableContainer, Exception exception);
}
