package top.spray.engine.step.executor.filter;

import top.spray.core.engine.props.SprayData;
import top.spray.engine.step.executor.SprayProcessStepExecutor;
import top.spray.engine.step.meta.SprayProcessStepMeta;

public interface SprayStepMetaFilter {
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
