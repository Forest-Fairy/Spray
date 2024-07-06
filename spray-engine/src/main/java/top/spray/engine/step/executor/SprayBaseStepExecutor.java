package top.spray.engine.step.executor;

import org.slf4j.MDC;
import top.spray.core.engine.props.SprayData;
import top.spray.core.util.SprayClassLoader;
import top.spray.engine.coordinate.coordinator.SprayProcessCoordinator;
import top.spray.engine.factory.SprayExecutorFactory;
import top.spray.engine.step.condition.SprayNextStepFilter;
import top.spray.engine.step.executor.cache.SprayCacheSupportExecutor;
import top.spray.engine.step.instance.SprayStepResultInstance;
import top.spray.engine.step.meta.SprayProcessStepMeta;

import java.util.Map;

/**
 * Define the executor of a process node
 */
public abstract class SprayBaseStepExecutor implements SprayProcessStepExecutor {
    protected String executorNameKey;
    private SprayProcessStepMeta stepMeta;
    private SprayProcessCoordinator coordinator;
    private SprayClassLoader classLoader;
    private SprayStepResultInstance stepResult;
    private long createTime;

    @Override
    public String getExecutorNameKey() {
        return this.executorNameKey;
    }


    @Override
    public void initOnlyAtCreate() {
        this.executorNameKey = SprayExecutorFactory.getExecutorNameKey(this.getCoordinator(), this.getMeta());
        this.stepResult = new SprayStepResultInstance(this.getCoordinator(), this);
        this.createTime = System.currentTimeMillis();
        this.initOnlyAtCreate0();
    }
    protected void initOnlyAtCreate0() {}



    @Override
    public long getCreateTime() {
        return this.createTime;
    }

    @Override
    public void setMeta(SprayProcessStepMeta meta) {
        this.stepMeta = meta;
    }
    @Override
    public void setCoordinator(SprayProcessCoordinator coordinator) {
        this.coordinator = coordinator;
    }
    @Override
    public void setClassLoader(SprayClassLoader classLoader) {
        this.classLoader = classLoader;
    }
    @Override
    public SprayProcessStepMeta getMeta() {
        return this.stepMeta;
    }
    @Override
    public SprayProcessCoordinator getCoordinator() {
        return this.coordinator;
    }
    @Override
    public SprayClassLoader getClassLoader() {
        return this.classLoader;
    }

    public SprayStepResultInstance getStepResult() {
        // create when init;
        return this.stepResult;
    }

    protected void publishData(SprayData data, boolean still) {
        this.publishData(data, still, null);
    }
    protected void publishData(SprayData data, boolean still, SprayNextStepFilter stepFilter) {
        this.getCoordinator().dispatch(this, stepFilter, data, still, false);
    }
    protected void publishDataAsync(SprayData data, boolean still) {
        this.publishDataAsync(data, still, null);
    }
    protected void publishDataAsync(SprayData data, boolean still, SprayNextStepFilter stepFilter) {
        this.getCoordinator().dispatch(this, stepFilter, data, still, true);
    }

    @Override
    public boolean needWait(SprayProcessStepExecutor fromExecutor, SprayData data, boolean still, Map<String, Object> processData) {
        // if the executor support to storage data in file then try
        if (this instanceof SprayCacheSupportExecutor storageSupportExecutor) {
            if (storageSupportExecutor.needCache(fromExecutor, data, still, processData)) {
                storageSupportExecutor.cache(fromExecutor, data, still, processData);
            }
        }
        return needWait0(fromExecutor, data, still, processData);
    }

    protected boolean needWait0(SprayProcessStepExecutor fromExecutor, SprayData data, boolean still, Map<String, Object> processData) {
        return false;
    }


    private synchronized void initBeforeRun() {
        MDC.put("transactionId", this.getCoordinator().getMeta().transactionId());
        MDC.put("executorId", this.getExecutorNameKey());
        initBeforeRun0();
    }
    protected synchronized void initBeforeRun0() {}

    @Override
    public void execute(SprayProcessStepExecutor fromExecutor, SprayData data, boolean still, Map<String, Object> processData) {
        initBeforeRun();
        this.execute0(fromExecutor, data, still, processData);
    }

    /**
     * a shaded execution method
     * @param fromExecutor the last executor
     * @param data data published by the last executor
     * @param still does it still have data to publish
     * @param processData a process data for current executor
     */
    protected abstract void execute0(SprayProcessStepExecutor fromExecutor, SprayData data, boolean still, Map<String, Object> processData);
}
