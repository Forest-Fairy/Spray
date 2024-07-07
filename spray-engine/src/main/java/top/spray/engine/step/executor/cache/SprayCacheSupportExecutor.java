package top.spray.engine.step.executor.cache;

import top.spray.core.engine.props.SprayData;
import top.spray.engine.prop.SprayVariableContainer;
import top.spray.engine.step.executor.SprayProcessStepExecutor;

public interface SprayCacheSupportExecutor extends SprayProcessStepExecutor {
    long getCurrentDataCount();
    boolean needCache(SprayVariableContainer variables, SprayProcessStepExecutor fromExecutor, SprayData data, boolean still);

    void cache(SprayVariableContainer variables, SprayProcessStepExecutor fromExecutor, SprayData data, boolean still);
}
