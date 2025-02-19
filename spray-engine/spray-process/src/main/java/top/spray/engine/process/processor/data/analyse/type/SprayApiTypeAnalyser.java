package top.spray.engine.process.processor.data.analyse.type;

import top.spray.common.cache.SprayCacheNotifier;
import top.spray.common.cache.SprayTimeCache;
import top.spray.engine.process.processor.data.analyse.direction.SprayDataDirection;
import top.spray.engine.process.processor.dispatch.coordinate.status.SprayCoordinatorStatus;
import top.spray.engine.process.processor.execute.step.executor.facade.SprayStepFacade;

import java.util.Map;
import java.util.concurrent.atomic.LongAdder;
import java.util.function.Supplier;

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
            return getCounterFromCache(executorFacade.executorNameKey(),
                    CacheNotifier.builder(this, executorFacade)::build);
        } else {
            return counter;
        }
    }

    private LongAdder getCounterFromCache(String executorNameKey, Supplier<SprayCacheNotifier> notifierSupplier) {
        return getFromCacheAndOrSupplierOne(notifierSupplier, executorNameKey, LongAdder::new);
    }

    private static class CacheNotifier implements SprayCacheNotifier {
        SprayApiTypeAnalyser analyser;
        SprayStepFacade executorFacade;
        CacheNotifier(SprayApiTypeAnalyser analyser, SprayStepFacade executorFacade) {
            this.analyser = analyser;
            this.executorFacade = executorFacade;
        }

        @Override
        public void notify(String key, Object value, int status) {
            if (status == SprayTimeCache.Status.EXPIRED
                    && !executorFacade.getCoordinator().runningStatus().getStatus().isEnd()) {
                analyser.setCache(this, key, value, 10);
            }
        }

        static NotifierBuilder builder(SprayApiTypeAnalyser analyser, SprayStepFacade executorFacade) {
            return new NotifierBuilder(analyser, executorFacade);
        }
        private static class NotifierBuilder {
            SprayApiTypeAnalyser analyser;
            SprayStepFacade executorFacade;

            public NotifierBuilder(SprayApiTypeAnalyser analyser, SprayStepFacade executorFacade) {
                this.analyser = analyser;
                this.executorFacade = executorFacade;
            }

            CacheNotifier build() { return new CacheNotifier(analyser, executorFacade); }
        }
    }

}
