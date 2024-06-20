package top.spray.engine.factory;

import top.spray.core.engine.props.SprayData;
import top.spray.core.util.SprayServiceUtil;
import top.spray.engine.step.executor.SprayProcessStepExecutor;
import top.spray.engine.step.handler.record.SprayRecordStrategyHandler;

public class SprayRecordStrategyHandlerFactory {
    public static SprayRecordStrategyHandler create(SprayProcessStepExecutor stepExecutor, String recordType,
                                                    SprayProcessStepExecutor fromExecutor, SprayData data, boolean still) {
        for (SprayRecordStrategyHandler sprayRecordStrategyHandler :
                SprayServiceUtil.loadServiceClassNameMapCache(SprayRecordStrategyHandler.class).values()) {
            if (sprayRecordStrategyHandler.canHandle(stepExecutor, recordType, fromExecutor, data, still)) {
                sprayRecordStrategyHandler.record(stepExecutor, recordType, fromExecutor, data, still);
                return sprayRecordStrategyHandler;
            }
        }
        return null;
    }
}
