package top.spray.engine.process.processor.data.analyse.type;

import top.spray.common.cache.SprayCacheNotifier;
import top.spray.common.cache.SprayTimeCache;
import top.spray.common.data.SprayData;
import top.spray.engine.process.infrastructure.analyse.SprayAnalysable;
import top.spray.engine.process.infrastructure.analyse.SprayAnalyseException;
import top.spray.engine.process.processor.runtime.SprayProcessConstant;
import top.spray.engine.process.processor.data.analyse.SprayDataAnalyseResult;
import top.spray.engine.process.processor.data.analyse.SprayDataAnalyser;
import top.spray.engine.process.processor.data.analyse.SprayDefaultDataAnalyseResult;
import top.spray.engine.process.processor.data.analyse.direction.SprayDataDirection;
import top.spray.engine.process.processor.execute.step.executor.facade.SprayStepFacade;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

public abstract class SprayAbstractDataTypeAnalyser implements SprayDataAnalyser<SprayStepFacade> {
    private final String name;
    private final int requiredInfoCount;
    private final List<String> otherRequiredInfoNames;

    protected SprayAbstractDataTypeAnalyser(String name, String... requiredParamNames) {
        this.name = name;
        this.requiredInfoCount = requiredParamNames == null ? 0 : requiredParamNames.length;
        this.otherRequiredInfoNames = requiredParamNames == null ? Collections.emptyList() : List.of(requiredParamNames);
        VALUES.add(this);
    }

    /**
     * @return prefix #sep direction
     */
    @Override
    public String analyserName() {
        return "DefaultAnalyser";
    }

    @Override
    public boolean isSupport(SprayAnalysable analysable) {
        return analysable instanceof SprayStepFacade;
    }

    @Override
    public SprayDataAnalyseResult analyse(SprayStepFacade analysable, Object... params) {
        if (params == null || params.length < 2
                || ! (params[0] instanceof SprayDataDirection direction)
                || ! (params[1] instanceof String dataId)) {
            throw new IllegalArgumentException("param is illegal");
        }
        Map<String, Object> paramMap = params.length > 3 ? (params[4] instanceof Map<?,?> map ? new SprayData(map) : null) : null;

        try {
            SprayDefaultDataAnalyseResult analyseResult =
                    this.analyse(analysable, direction, dataId, paramMap);
            analyseResult.getInfo().put("direction", direction.getName());
            analyseResult.getInfo().put("type", this.getName());
            return analyseResult;
        } catch (Exception e) {
            if (e instanceof SprayAnalyseException analyseException) {
                throw analyseException;
            }
            throw new SprayAnalyseException(e, "analyse.unknown",
                    this.analyserName(), direction.getName(),
                    analysable.executorNameKey(), paramMap, e.getMessage());
        }
    }


    public final String getName() {
        return name;
    }

    public final List<String> getOtherRequiredInfoNames() {
        return otherRequiredInfoNames;
    }

    protected <T> T lackOfParam(String... paramNames) {
        throw new IllegalArgumentException("unable to analyse " + this.getName() + ", because some required params are lacking! " +
                "such as" + String.join(",", paramNames));
    }

    private SprayDefaultDataAnalyseResult analyse(SprayStepFacade executorFacade, SprayDataDirection direction, String dataId, Map<String, Object> paramMap) {
        if (this.requiredInfoCount > 0) {
            if (! checkRequiredParams(paramMap)) {
                throw new IllegalArgumentException("unable to analyse " + this.getName() + ", because some required params are lacking! "
                        + "current " + this.analyserName() + ": " + paramMap);
            }
        }
        if (paramMap == null) {
            paramMap = Collections.emptyMap();
        }
        Map<String, Object> analyseInfo = this.doAnalyse(executorFacade, direction, paramMap);
        if (analyseInfo == null) {
            analyseInfo = Collections.emptyMap();
        }
        return new SprayDefaultDataAnalyseResult(this, dataId, direction, analyseInfo);
    }

    protected abstract Map<String, Object> doAnalyse(SprayStepFacade executorFacade, SprayDataDirection direction, Map<String, Object> paramMap);

    private boolean checkRequiredParams(Map<String, Object> paramMap) {
        if (this.requiredInfoCount == 0) {
            return true;
        }
        if (paramMap == null || paramMap.size() < this.requiredInfoCount) {
            return false;
        }
        for (String paramName : this.otherRequiredInfoNames) {
            if (! paramMap.containsKey(paramName)) {
                return false;
            }
        }
        return true;
    }

    protected void setCache(SprayCacheNotifier notifier, String key, Object value, int expireMinutes) {
        SprayTimeCache.set(notifier, SprayProcessConstant.NameSpace.EXECUTOR_NAMESPACE, key, value,
                TimeUnit.MINUTES.toMillis(expireMinutes), SprayTimeCache.ExpireStrategy.REFRESH_WHEN_ACCESS);
    }

    protected <T> T getFromCacheAndOrSupplierOne(Supplier<SprayCacheNotifier> notifierSupplier, String key, Supplier<T> supplier) {
        return getFromCacheAndOrSupplierOne(notifierSupplier, key, supplier, 10);
    }

    protected <T> T getFromCacheAndOrSupplierOne(Supplier<SprayCacheNotifier> notifierSupplier, String key, Supplier<T> supplier, int expireMinutes) {
        return SprayTimeCache.computeIfAbsent(notifierSupplier, SprayProcessConstant.NameSpace.EXECUTOR_NAMESPACE,
                key, supplier, TimeUnit.MINUTES.toMillis(expireMinutes),
                SprayTimeCache.ExpireStrategy.REFRESH_WHEN_ACCESS);
    }

    protected Object removeCache(String key) {
        return SprayTimeCache.remove(SprayProcessConstant.NameSpace.EXECUTOR_NAMESPACE, key);
    }

    private static final List<SprayAbstractDataTypeAnalyser> VALUES = new LinkedList<>();
    public static SprayAbstractDataTypeAnalyser of(String name) {
        return VALUES.stream()
                .filter(type -> type.getName().equals(name))
                .findAny().orElse(null);
    }
}
