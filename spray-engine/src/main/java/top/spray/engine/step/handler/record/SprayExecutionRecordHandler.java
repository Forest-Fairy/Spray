package top.spray.engine.step.handler.record;

import top.spray.core.engine.props.SprayData;
import top.spray.core.engine.types.data.execute.record.SprayExecutionRecordType;
import top.spray.core.util.SprayServiceUtil;
import top.spray.engine.coordinate.meta.SprayProcessCoordinatorMeta;
import top.spray.engine.step.meta.SprayProcessStepMeta;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public interface SprayExecutionRecordHandler {
    static List<SprayExecutionRecordHandler> create(SprayProcessCoordinatorMeta coordinatorMeta, SprayProcessStepMeta executorMeta) {
        Map<String, SprayExecutionRecordHandler> handlerMap = SprayServiceUtil.loadServiceClassNameMapCache(SprayExecutionRecordHandler.class);
        return handlerMap.values().stream().filter(handler -> handler.support(coordinatorMeta, executorMeta)).collect(Collectors.toList());
    }

    boolean support(SprayProcessCoordinatorMeta coordinatorMeta, SprayProcessStepMeta executorMeta);

    void record(SprayProcessStepMeta stepExecutor, SprayExecutionRecordType recordType,
                SprayProcessStepMeta fromExecutor, SprayData data, boolean still, Throwable error);


}
