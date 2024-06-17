package top.spray.engine.step.handler.record;

import top.spray.core.engine.props.SprayData;
import top.spray.engine.step.executor.SprayProcessStepExecutor;

public interface SprayRecordStrategyHandler {
    boolean canHandle(SprayProcessStepExecutor stepExecutor, String recordType,
                      SprayProcessStepExecutor fromExecutor, SprayData data, boolean still);

    void record(SprayProcessStepExecutor stepExecutor, String recordType,
                 SprayProcessStepExecutor fromExecutor, SprayData data, boolean still);


}
