package top.spray.engine.step.executor.cache;

import top.spray.core.engine.props.SprayData;
import top.spray.engine.prop.SprayExecutorVariable;
import top.spray.engine.step.executor.SprayProcessStepExecutor;

public interface SprayCacheSupportExecutor extends SprayProcessStepExecutor {
    long getCurrentDataCount();
    boolean needCache(SprayExecutorVariable variables, SprayProcessStepExecutor fromExecutor, SprayData data, boolean still);

    void cache(SprayExecutorVariable variables, SprayProcessStepExecutor fromExecutor, SprayData data, boolean still);
}
