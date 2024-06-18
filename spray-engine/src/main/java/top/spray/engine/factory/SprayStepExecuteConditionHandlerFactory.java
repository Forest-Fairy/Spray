package top.spray.engine.factory;

import top.spray.core.util.SprayServiceUtil;
import top.spray.engine.step.condition.SprayStepExecuteConditionFilter;
import top.spray.engine.step.handler.SprayExecutorHandler;
import top.spray.engine.step.handler.filter.SprayStepExecuteConditionFilterHandler;
import top.spray.engine.step.meta.SprayProcessStepMeta;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class SprayStepExecuteConditionHandlerFactory {
    public static Collection<SprayStepExecuteConditionFilter> createFilters(SprayProcessStepMeta stepMeta) {
        Map<String, SprayStepExecuteConditionFilterHandler> filterHandlerMap = SprayServiceUtil.loadServiceClassNameMapCache(SprayStepExecuteConditionFilterHandler.class);
        return filterHandlerMap.values().stream()
                .map(handler -> handler.createFilters(stepMeta))
                .reduce(new ArrayList<>(), (i, o) -> {
                    i.addAll(o);
                    return i;
                });
    }
}
