package top.spray.engine.factory;

import top.spray.core.util.SprayServiceUtil;
import top.spray.engine.step.handler.record.SprayExecutionRecordHandler;
import top.spray.engine.step.instance.SprayStepResultInstance;

public class SprayRecordStrategyHandlerFactory {
    public static SprayExecutionRecordHandler create(SprayStepResultInstance instance) {
        for (SprayExecutionRecordHandler sprayRecordStrategyHandler :
                SprayServiceUtil.loadServiceClassNameMapCache(SprayExecutionRecordHandler.class).values()) {
            if (sprayRecordStrategyHandler.support(instance.getCoordinatorMeta(), instance.getExecutorMeta())) {
                return sprayRecordStrategyHandler;
            }
        }
        return null;
    }
}
