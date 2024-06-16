package top.spray.engine.step.condition;

import top.spray.core.engine.props.SprayData;
import top.spray.engine.step.executor.SprayProcessStepExecutor;
import top.spray.engine.step.meta.SprayProcessStepMeta;

/**
 * reg the executor that can be executed
 */
public interface SprayStepConditionalFilter {
    /**
     * to filter the data
     * @param fromExecutor the data comes from
     * @param data data
     * @param still still has data
     * @param nextStepMeta next step's meta
     * @return false means pass
     */
    boolean filter(SprayProcessStepExecutor fromExecutor, SprayData data, boolean still, SprayProcessStepMeta nextStepMeta);
}
