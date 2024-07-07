package top.spray.engine.step.executor.cache;

import top.spray.core.engine.props.SprayData;
import top.spray.engine.prop.SprayRuntimeVariables;
import top.spray.engine.step.executor.SprayProcessStepExecutor;

import java.util.Map;

public interface SprayCacheSupportExecutor extends SprayProcessStepExecutor {
    long getCurrentDataCount();
    boolean needCache(SprayRuntimeVariables variables, SprayProcessStepExecutor fromExecutor, SprayData data, boolean still);

    void cache(SprayRuntimeVariables variables, SprayProcessStepExecutor fromExecutor, SprayData data, boolean still);
}
