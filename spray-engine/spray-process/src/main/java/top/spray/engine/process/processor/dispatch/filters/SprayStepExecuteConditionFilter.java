package top.spray.engine.process.processor.dispatch.filters;

import top.spray.common.data.SprayData;
import top.spray.engine.process.processor.dispatch.coordinate.coordinator.SprayProcessCoordinator;
import top.spray.engine.process.processor.execute.step.meta.SprayOptionalData;
import top.spray.engine.process.processor.execute.step.meta.SprayProcessExecuteStepMeta;

import java.util.Collection;
import java.util.List;

/**
 * judge by the coordinator
 */
public interface SprayStepExecuteConditionFilter {
    /**
     * to filter the data
     *  - like ① -> ②，that means fromExecutor is 1 and stepMetaForExecuting is 2
     * @param coordinator coordinator
     * @param variableContainerIdentityDataKey current process variableContainer's identityDataKey
     * @param optionalData optionalData { dataGenerator, data, isStill } nullable
     * @param nodeMeta the meta belong to the node which is going to be created or executed
     * @return false means pass
     */
    boolean filterBeforeExecute(SprayProcessCoordinator coordinator, String variableContainerIdentityDataKey, SprayOptionalData optionalData, SprayProcessExecuteStepMeta nodeMeta);

    class Factory {
        public static Collection<SprayStepExecuteConditionFilter> createFilters(SprayProcessExecuteStepMeta stepMeta) {
            SprayData metaContainer = stepMeta.getMetaContainer();
            Object fs = metaContainer.get("ConditionFilters");
            if ((fs instanceof List) && (!((List<?>) fs).isEmpty())) {

            }
        }
    }
}
