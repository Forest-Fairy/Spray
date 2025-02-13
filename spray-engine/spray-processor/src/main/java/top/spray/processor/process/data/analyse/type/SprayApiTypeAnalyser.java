package top.spray.processor.process.data.analyse.type;

import top.spray.processor.process.data.analyse.direction.SprayDataDirection;
import top.spray.processor.process.execute.step.executor.facade.SprayStepFacade;

import java.util.Map;
import java.util.concurrent.atomic.LongAdder;

public class SprayApiTypeAnalyser extends SprayAbstractDataTypeAnalyser {
    public static final SprayApiTypeAnalyser INSTANCE = new SprayApiTypeAnalyser();
    // urL method requestParam
    private SprayApiTypeAnalyser() {
        super("API", "url", "method", "requestParam");
    }

    @Override
    protected Map<String, Object> doAnalyse(SprayStepFacade executorFacade, SprayDataDirection direction, Map<String, Object> paramMap) {
        String url = String.valueOf(paramMap.get("url"));
        String method = String.valueOf(paramMap.get("method"));
        String requestParam = String.valueOf(paramMap.get("requestParam"));
        LongAdder counter = getCounter(executorFacade, paramMap);
        counter.increment();
        return Map.of(
                "url", url,
                "method", method,
                "requestParam", requestParam,
                "count", counter.longValue()
        );
    }

    public LongAdder getCounter(SprayStepFacade executorFacade, Map<String, Object> params) {
        LongAdder counter = (LongAdder) params.get("counter");
        if (counter == null) {
            return getCounterFromCache(executorFacade.executorNameKey());
        } else {
            return counter;
        }
    }

    private LongAdder getCounterFromCache(String executorNameKey) {
        return getFromCacheAndOrSupplierOne(executorNameKey, LongAdder::new);
    }
}
