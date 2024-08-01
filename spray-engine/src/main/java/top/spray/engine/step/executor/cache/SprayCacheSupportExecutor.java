package top.spray.engine.step.executor.cache;

import top.spray.core.engine.props.SprayData;
import top.spray.engine.prop.SprayVariableContainer;
import top.spray.engine.step.executor.SprayExecutorDefinition;

public interface SprayCacheSupportExecutor extends SprayExecutorDefinition {
    long getCurrentDataCount();
    boolean needCache(SprayVariableContainer variables, SprayExecutorDefinition fromExecutor, SprayData data, boolean still);

    void cache(SprayVariableContainer variables, SprayExecutorDefinition fromExecutor, SprayData data, boolean still);
}
