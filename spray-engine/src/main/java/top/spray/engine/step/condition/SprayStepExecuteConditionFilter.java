package top.spray.engine.step.condition;

import top.spray.core.engine.props.SprayData;
import top.spray.engine.step.executor.SprayProcessStepExecutor;
import top.spray.engine.step.meta.SprayProcessStepMeta;

/**
 * reg the executor that can be executed
 */
public interface SprayStepExecuteConditionFilter {
    /**
     * to filter the data
     *  - like ① -> ②，that means fromExecutor is 1 and stepMetaForExecuting is 2
     * @param fromExecutor the data comes from
     * @param data data
     * @param still still has data
     * @param stepMetaForExecuting target step's meta
     * @return false means pass
     */
    boolean executableForMe(SprayProcessStepExecutor fromExecutor, SprayData data, boolean still, SprayProcessStepMeta stepMetaForExecuting);

}
