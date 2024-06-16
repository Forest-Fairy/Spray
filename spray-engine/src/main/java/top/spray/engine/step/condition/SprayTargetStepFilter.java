package top.spray.engine.step.condition;

import top.spray.core.engine.props.SprayData;
import top.spray.engine.step.executor.SprayProcessStepExecutor;
import top.spray.engine.step.meta.SprayProcessStepMeta;

public interface SprayTargetStepFilter {
    /**
     * is next step need to execute with
     * @param fromExecutor the data comes from
     * @param data data
     * @param still still has data
     * @param nextStepMeta next step's meta
     * @return false means skip
     */
    boolean canBeExecute(SprayProcessStepExecutor fromExecutor, SprayData data, boolean still, SprayProcessStepMeta nextStepMeta);
}
