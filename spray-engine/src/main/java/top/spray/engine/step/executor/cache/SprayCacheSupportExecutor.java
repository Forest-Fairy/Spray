package top.spray.engine.step.executor.cache;

import top.spray.core.engine.props.SprayData;
import top.spray.engine.step.executor.SprayProcessStepExecutor;

import java.util.Map;

public interface SprayCacheSupportExecutor extends SprayProcessStepExecutor {
    long getCurrentDataCount();
    boolean needCache(SprayProcessStepExecutor fromExecutor, SprayData data, boolean still, Map<String, Object> processData);

    void cache(SprayProcessStepExecutor fromExecutor, SprayData data, boolean still, Map<String, Object> processData);
}
