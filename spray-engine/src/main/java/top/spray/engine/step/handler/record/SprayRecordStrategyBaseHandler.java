package top.spray.engine.step.handler.record;

import top.spray.core.engine.props.SprayData;
import top.spray.engine.step.executor.SprayProcessStepExecutor;

public abstract class SprayRecordStrategyBaseHandler implements SprayRecordStrategyHandler {
    protected final String typeName;
    public SprayRecordStrategyBaseHandler(String typeName) {
        this.typeName = typeName;
    }

    @Override
    public boolean canHandle(SprayProcessStepExecutor stepExecutor, String recordType, SprayProcessStepExecutor fromExecutor, SprayData data, boolean still) {
        return false;
    }

    @Override
    public void record(SprayProcessStepExecutor stepExecutor, String recordType, SprayProcessStepExecutor fromExecutor, SprayData data, boolean still) {

    }
}
