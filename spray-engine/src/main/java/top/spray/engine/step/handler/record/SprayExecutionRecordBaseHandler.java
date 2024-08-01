package top.spray.engine.step.handler.record;

import top.spray.core.engine.props.SprayData;
import top.spray.core.engine.types.data.execute.record.SprayExecutionRecordType;
import top.spray.engine.coordinate.meta.SprayProcessCoordinatorMeta;
import top.spray.engine.step.executor.SprayExecutorDefinition;
import top.spray.engine.step.meta.SprayProcessStepMeta;

import java.util.Set;

public abstract class SprayExecutionRecordBaseHandler implements SprayExecutionRecordHandler {
    public SprayExecutionRecordBaseHandler() {
    }

    @Override
    public boolean support(SprayProcessCoordinatorMeta coordinatorMeta, SprayProcessStepMeta executorMeta) {
        Set<String> strategies = Set.of(executorMeta.getString("recordStrategies", "").split(","));
        return strategies.contains("default");
    }

    @Override
    public void record(SprayExecutorDefinition recordExecutor, SprayExecutionRecordType recordType, SprayExecutorDefinition fromExecutor, SprayData data, boolean still) {
        // TODO log with something
    }
}
