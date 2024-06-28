package top.spray.engine.step.handler.filter;
import com.alibaba.fastjson2.JSONObject;
import top.spray.core.engine.props.SprayData;
import top.spray.core.util.SprayFastJsonUtil;
import top.spray.core.util.SprayServiceUtil;
import top.spray.engine.step.condition.SprayStepExecuteConditionFilter;
import top.spray.engine.step.handler.SprayExecutorHandler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface SprayStepExecuteConditionHelper extends SprayExecutorHandler {
    Collection<SprayStepExecuteConditionFilter> createFilters(List<JSONObject> filtersJson);

    static Collection<SprayStepExecuteConditionFilter> createFilters(SprayData stepData) {
        Map<String, SprayStepExecuteConditionHelper> filterHandlerMap =
                SprayServiceUtil.loadServiceClassNameMapCache(
                        SprayStepExecuteConditionHelper.class);
        JSONObject jsonObject = SprayFastJsonUtil.parseJson(SprayFastJsonUtil.toJson(stepData), JSONObject.class);
        List<JSONObject> executeConditionFilter =
                !jsonObject.containsKey("executeConditionFilter") ? new ArrayList<>(0) :
                        jsonObject.getList("executeConditionFilter", JSONObject.class);
        return filterHandlerMap.values().stream()
                .map(handler -> handler.createFilters(executeConditionFilter))
                .reduce(new ArrayList<>(), (i, o) -> {
                    i.addAll(o);
                    return i;
                });
    }
}
