package top.spray.engine.step.handler.runner;

import top.spray.core.engine.props.SprayData;
import top.spray.core.util.SprayServiceUtil;
import top.spray.engine.step.executor.SprayProcessStepExecutor;
import top.spray.engine.step.handler.SprayExecutorHandler;

import java.util.Map;

public interface SprayExecutorAutoRunner extends SprayExecutorHandler {
    static void autoRun(SprayProcessStepExecutor executor, SprayData data, boolean still, Map<String, Object> processData) {
        for (SprayExecutorAutoRunner handler : SprayServiceUtil.loadServiceClassNameMapCache(SprayExecutorAutoRunner.class).values()) {
            if (handler.doRun(executor, data, still, processData)) {
                break;
            }
        }
    }

    boolean doRun(SprayProcessStepExecutor fromExecutor, SprayData data, boolean still, Map<String, Object> processData) ;
}
