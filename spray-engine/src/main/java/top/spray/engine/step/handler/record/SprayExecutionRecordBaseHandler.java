package top.spray.engine.step.handler.record;

import top.spray.core.engine.props.SprayData;
import top.spray.core.engine.types.data.execute.record.SprayExecutionRecordType;
import top.spray.engine.coordinate.meta.SprayProcessCoordinatorMeta;
import top.spray.engine.step.executor.SprayProcessStepExecutor;
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
    public void record(SprayProcessStepExecutor recordExecutor, SprayExecutionRecordType recordType, SprayProcessStepExecutor fromExecutor, SprayData data, boolean still) {
        // TODO log with something
    }
}
