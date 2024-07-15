package top.spray.engine.step.executor;

import org.slf4j.MDC;
import top.spray.core.engine.props.SprayData;
import top.spray.core.util.SprayClassLoader;
import top.spray.engine.coordinate.coordinator.SprayProcessCoordinator;
import top.spray.engine.prop.SprayVariableContainer;
import top.spray.engine.step.condition.SprayNextStepFilter;
import top.spray.engine.step.executor.cache.SprayCacheSupportExecutor;
import top.spray.engine.step.instance.SprayStepResultInstance;
import top.spray.engine.step.meta.SprayProcessStepMeta;

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
    public void initOnlyAtCreate() {
        this.executorNameKey = this.getCoordinator().getExecutorNameKey(this.getMeta());
        this.stepResult = new SprayStepResultInstance(this.getCoordinator(), this);
        this.createTime = System.currentTimeMillis();
        this.initOnlyAtCreate0();
    }
    protected void initOnlyAtCreate0() {}


    @Override
    public long runningCount() {
        return this.getStepResult().runningCount();
    }

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

    protected void publishData(SprayVariableContainer variableContainer, SprayData data, boolean still) {
        this.publishData(variableContainer, data, still, null);
    }
    protected void publishData(SprayVariableContainer variableContainer, SprayData data, boolean still, SprayNextStepFilter stepFilter) {
        this.getCoordinator().dispatch(variableContainer, this, stepFilter, data, still, false);
    }
    protected void publishDataAsync(SprayVariableContainer variableContainer, SprayData data, boolean still) {
        this.publishDataAsync(variableContainer, data, still, null);
    }
    protected void publishDataAsync(SprayVariableContainer variableContainer, SprayData data, boolean still, SprayNextStepFilter stepFilter) {
        this.getCoordinator().dispatch(variableContainer, this, stepFilter, data, still, true);
    }

    @Override
    public boolean needWait(SprayVariableContainer variables, SprayProcessStepExecutor fromExecutor, SprayData data, boolean still) {
        // if the executor support to storage data in file then try
        if (this instanceof SprayCacheSupportExecutor storageSupportExecutor) {
            if (storageSupportExecutor.needCache(variables, fromExecutor, data, still)) {
                storageSupportExecutor.cache(variables, fromExecutor, data, still);
            }
        }
        return needWait0(variables, fromExecutor, data, still);
    }

    protected boolean needWait0(SprayVariableContainer variables, SprayProcessStepExecutor fromExecutor, SprayData data, boolean still) {
        return false;
    }


    private synchronized void initBeforeRun() {
        MDC.put("transactionId", this.getCoordinator().getMeta().transactionId());
        MDC.put("executorId", this.executorNameKey);
        initBeforeRun0();
    }
    protected synchronized void initBeforeRun0() {}

    @Override
    public void execute(SprayVariableContainer variables, SprayProcessStepExecutor fromExecutor, SprayData data, boolean still) {
        initBeforeRun();
        try {
            this.getStepResult().incrementRunningCount();
            this.execute0(variables, fromExecutor, data, still);
        } catch (Throwable e) {

        } finally {
            this.getStepResult().decrementRunningCount();
        }
    }

    /**
     * a shaded execution method
     * @param variables a variable namespace for current executor
     * @param fromExecutor the last executor
     * @param data data published by the last executor
     * @param still does it still have data to publish
     */
    protected abstract void execute0(SprayVariableContainer variables, SprayProcessStepExecutor fromExecutor, SprayData data, boolean still);
}
