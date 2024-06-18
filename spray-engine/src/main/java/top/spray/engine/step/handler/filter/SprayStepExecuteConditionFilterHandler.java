package top.spray.engine.step.handler.filter;
import top.spray.core.engine.props.SprayData;
import top.spray.engine.step.condition.SprayStepExecuteConditionFilter;
import top.spray.engine.step.executor.SprayProcessStepExecutor;
import top.spray.engine.step.handler.SprayExecutorHandler;
import top.spray.engine.step.meta.SprayProcessStepMeta;

import java.util.Collection;

public interface SprayStepExecuteConditionFilterHandler extends SprayExecutorHandler {
    Collection<SprayStepExecuteConditionFilter> createFilters(SprayProcessStepMeta stepMeta);
}
