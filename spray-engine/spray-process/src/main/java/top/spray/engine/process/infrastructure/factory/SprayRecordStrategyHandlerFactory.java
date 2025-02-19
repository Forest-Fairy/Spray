package top.spray.engine.process.infrastructure.factory;

import top.spray.common.bean.SprayServiceUtil;
import top.spray.engine.process.processor.execute.step.status.SprayStepStatusInstance;

public class SprayRecordStrategyHandlerFactory {
    public static SprayExecutionRecordHandler create(SprayStepStatusInstance instance) {
        for (SprayExecutionRecordHandler sprayRecordStrategyHandler :
                SprayServiceUtil.loadServiceClassNameMapCache(SprayExecutionRecordHandler.class).values()) {
            if (sprayRecordStrategyHandler.support(instance.getCoordinatorMeta(), instance.getExecutorMeta())) {
                return sprayRecordStrategyHandler;
            }
        }
        return null;
    }
}
