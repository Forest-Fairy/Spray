//package top.spray.engine.process.dispatch.result;
//
//import top.spray.core.prop.SprayData;
//import top.spray.processor.process.dispatch.coordinate.meta.SprayProcessCoordinatorMeta;
//import top.spray.processor.process.dispatch.event.SprayDataDispatchResultHandler_PropertiesFile;
//import top.spray.processor.process.data.event.impl.SprayDataDispatchResultType;
//import top.spray.core.util.SprayServiceUtil;
//import top.spray.processor.infrustructure.prop.SprayVariableContainer;
//import top.spray.processor.process.execute.step.executor.facade.SprayStepExecutorFacade;
//import top.spray.processor.process.execute.step.meta.SprayProcessExecuteStepMeta;
//
//import java.util.List;
//import java.util.Map;
//
//public interface SprayDataDispatchResultHandler {
//    static SprayDataDispatchResultHandler get(SprayProcessCoordinatorMeta coordinatorMeta) {
//        Map<String, SprayDataDispatchResultHandler> handlerMap = SprayServiceUtil.loadServiceClassNameMapCache(SprayDataDispatchResultHandler.class);
//        return handlerMap.values().stream()
//                .filter(handler -> handler.canDeal(coordinatorMeta))
//                .findFirst().orElse(SprayDataDispatchResultHandler_PropertiesFile.INSTANCE);
//    }
//
//    boolean canDeal(SprayProcessCoordinatorMeta coordinatorMeta);
//
//    String computeDataKey(SprayProcessCoordinatorMeta coordinatorMeta, SprayVariableContainer variables,
//                          SprayStepExecutorFacade fromExecutor, SprayData data, boolean still,
//                          SprayProcessExecuteStepMeta nextMeta);
//
//    void setDispatchResult(SprayProcessCoordinatorMeta coordinatorMeta, SprayVariableContainer variables,
//                           SprayStepExecutorFacade fromExecutor, SprayData data, boolean still,
//                           SprayProcessExecuteStepMeta nextMeta, String dataKey, SprayDataDispatchResultType dataDispatchStatus) ;
//
//    List<SprayDataDispatchResultType> getDispatchResult(SprayProcessCoordinatorMeta coordinatorMeta, String dataKey);
//
//
//    void whenCoordinatorShutdown(SprayProcessCoordinatorMeta coordinatorMeta);
//
//
//}
