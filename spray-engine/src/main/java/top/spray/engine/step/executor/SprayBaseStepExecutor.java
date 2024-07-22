package top.spray.engine.step.executor;

import org.slf4j.MDC;
import top.spray.core.engine.props.SprayData;
import top.spray.core.thread.SprayPoolExecutor;
import top.spray.core.thread.SprayPoolExecutorBuilder;
import top.spray.core.util.SprayClassLoader;
import top.spray.engine.coordinate.coordinator.SprayProcessCoordinator;
import top.spray.engine.exception.SprayExecuteError;
import top.spray.engine.prop.SprayVariableContainer;
import top.spray.engine.step.condition.SprayNextStepFilter;
import top.spray.engine.step.executor.cache.SprayCacheSupportExecutor;
import top.spray.engine.step.instance.SprayStepResultInstance;
import top.spray.engine.step.meta.SprayProcessStepMeta;

import java.util.concurrent.TimeUnit;

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
    private SprayPoolExecutor pool;


    @Override
    public void initOnlyAtCreate() {
        this.executorNameKey = this.getExecutorNameKey();
        this.stepResult = new SprayStepResultInstance(this.getCoordinator(), this);
        this.createTime = System.currentTimeMillis();
        if (this.stepMeta.isAsync() && this.getCoordinator().getMeta().asyncSupport()) {
            this.pool = new SprayPoolExecutorBuilder(
                    this.stepMeta.coreThreadCount(),
                    this.stepMeta.maxThreadCount(),
                    this.stepMeta.threadAliveTime(),
                    this.stepMeta.threadAliveTimeUnit(),
                    SprayPoolExecutor.newDefaultFactory(),
                    this.stepMeta.queueCapacity()).build();
        } else {
            this.pool = null;
        }
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
        this.getCoordinator().dispatch(variableContainer, this, stepFilter, data, still);
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
        if (this.getThreadPoll() != null) {
            this.getThreadPoll().submit(() -> {
                doExecute(variables, fromExecutor, data, still);
            });
        } else {
            doExecute(variables, fromExecutor, data, still);
        }
    }

    private void doExecute(SprayVariableContainer variables, SprayProcessStepExecutor fromExecutor, SprayData data, boolean still) {
        Thread.currentThread().setContextClassLoader(this.getClassLoader());
        initBeforeRun();
        try {
            this.getStepResult().incrementRunningCount();
            this._execute(variables, fromExecutor, data, still);
        } catch (Throwable e) {
            throw new SprayExecuteError(this, e);
        } finally {
            this.getStepResult().decrementRunningCount();
        }
    }

    @Override
    public int varCopyMode() {
        return this.stepMeta.varCopyMode();
    }

    @Override
    public SprayPoolExecutor getThreadPoll() {
        return this.pool;
    }


    /**
     * a shaded execution method
     * @param variables a variable namespace for current executor
     * @param fromExecutor the last executor
     * @param data data published by the last executor
     * @param still does it still have data to publish
     */
    protected abstract void _execute(SprayVariableContainer variables, SprayProcessStepExecutor fromExecutor, SprayData data, boolean still);

    /**
     * blocked until all the before executions done
     */
    protected void waitUntilBeforeExecutionDone() {
        while (this.runningCount() > 0) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new SprayExecuteError(this, e);
            }
        }
        if (Thread.currentThread().isInterrupted()) {
            throw new SprayExecuteError(this, new InterruptedException());
        }
    }
}
