package top.spray.engine.coordinate.handler.result;

import top.spray.core.engine.props.SprayData;
import top.spray.core.engine.types.data.dispatch.result.SprayEventDispatchResultStatus;
import top.spray.core.util.SprayServiceUtil;
import top.spray.engine.coordinate.meta.SprayProcessCoordinatorMeta;
import top.spray.engine.prop.SprayVariableContainer;
import top.spray.engine.step.executor.SprayExecutorDefinition;
import top.spray.engine.step.meta.SprayProcessStepMeta;

import java.util.List;
import java.util.Map;

public interface SprayDataDispatchResultHandler {
    static SprayDataDispatchResultHandler get(SprayProcessCoordinatorMeta coordinatorMeta) {
        Map<String, SprayDataDispatchResultHandler> handlerMap = SprayServiceUtil.loadServiceClassNameMapCache(SprayDataDispatchResultHandler.class);
        return handlerMap.values().stream()
                .filter(handler -> handler.canDeal(coordinatorMeta))
                .findFirst().orElse(SprayDataDispatchResultHandler_PropertiesFile.INSTANCE);
    }

    boolean canDeal(SprayProcessCoordinatorMeta coordinatorMeta);
    String computeDataKey(SprayProcessCoordinatorMeta coordinatorMeta, SprayVariableContainer variables,
                          SprayExecutorDefinition fromExecutor, SprayData data, boolean still,
                          SprayProcessStepMeta nextMeta);
    void setDispatchResult(SprayProcessCoordinatorMeta coordinatorMeta, SprayVariableContainer variables,
                           SprayExecutorDefinition fromExecutor, SprayData data, boolean still,
                           SprayProcessStepMeta nextMeta, String dataKey, SprayEventDispatchResultStatus dataDispatchStatus) ;
    List<SprayEventDispatchResultStatus> getDispatchResult(SprayProcessCoordinatorMeta coordinatorMeta, String dataKey);

    void whenCoordinatorShutdown(SprayProcessCoordinatorMeta coordinatorMeta);

}
