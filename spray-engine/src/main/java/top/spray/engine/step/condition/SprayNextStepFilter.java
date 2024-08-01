package top.spray.engine.step.condition;

import top.spray.core.engine.props.SprayData;
import top.spray.engine.step.executor.SprayExecutorDefinition;
import top.spray.engine.step.meta.SprayProcessStepMeta;

public interface SprayNextStepFilter {
    /**
     * is next step need to execute with
     * @param current the data comes from current step executor
     * @param data data
     * @param still still has data
     * @param nextStepMeta next step's meta
     * @return false means skip
     */
    boolean executableForNext(SprayExecutorDefinition current, SprayData data, boolean still, SprayProcessStepMeta nextStepMeta);
}
