package top.spray.engine.step.executor;

import org.slf4j.MDC;
import top.spray.core.engine.props.SprayData;
import top.spray.core.thread.SprayPoolExecutor;
import top.spray.engine.thread.SprayPoolExecutorBuilder;
import top.spray.core.util.SprayClassLoader;
import top.spray.engine.coordinate.coordinator.SprayProcessCoordinator;
import top.spray.engine.exception.SprayExecuteException;
import top.spray.engine.prop.SprayVariableContainer;
import top.spray.engine.step.condition.SprayNextStepFilter;
import top.spray.engine.step.executor.cache.SprayCacheSupportExecutor;
import top.spray.engine.step.instance.SprayStepResultInstance;
import top.spray.engine.step.meta.SprayProcessStepMeta;

/**
 * Define the executor of a process node
 */
public abstract class SprayBaseStepExecutor implements SprayProcessStepExecutor {
    private SprayProcessStepMeta stepMeta;
    private SprayProcessCoordinator coordinator;
    private SprayClassLoader classLoader;
    private SprayStepResultInstance stepResult;
    private long createTime;
    private SprayPoolExecutor pool;


    @Override
    public synchronized final boolean initOnlyAtCreate() {
        if (createTime != 0) {
            return false;
        }
        this.stepResult = new SprayStepResultInstance(
                this.getCoordinator().getMeta(),
                this.getMeta(), this.getClassLoader());
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
        return true;
    }
    protected void initOnlyAtCreate0() {}

    @Override
    public final long runningCount() {
        return this.getStepResult().runningCount();
    }

    @Override
    public final long getCreateTime() {
        return this.createTime;
    }

    @Override
    public final void setMeta(SprayProcessStepMeta meta) {
        this.stepMeta = meta;
    }
    @Override
    public final void setCoordinator(SprayProcessCoordinator coordinator) {
        this.coordinator = coordinator;
    }
    @Override
    public final void setClassLoader(SprayClassLoader classLoader) {
        this.classLoader = classLoader;
    }
    @Override
    public final SprayProcessStepMeta getMeta() {
        return this.stepMeta;
    }
    @Override
    public final SprayProcessCoordinator getCoordinator() {
        return this.coordinator;
    }
    @Override
    public final SprayClassLoader getClassLoader() {
        return this.classLoader;
    }

    public final SprayStepResultInstance getStepResult() {
        return this.stepResult;
    }

    protected final void publishData(SprayVariableContainer variableContainer, SprayData data, boolean still) {
        this.publishData(variableContainer, data, still, null);
    }
    protected final void publishData(SprayVariableContainer variableContainer, SprayData data, boolean still, SprayNextStepFilter stepFilter) {
        this.getCoordinator().dispatch(variableContainer, stepFilter, this, data, still);
    }

    @Override
    public final boolean needWait(SprayVariableContainer variables, SprayProcessStepExecutor fromExecutor, SprayData data, boolean still) {
        // if the executor support to storage data in file then try
        if (this instanceof SprayCacheSupportExecutor cacheSupportExecutor) {
            if (cacheSupportExecutor.needCache(variables, fromExecutor, data, still)) {
                cacheSupportExecutor.cache(variables, fromExecutor, data, still);
            }
        }
        return needWait0(variables, fromExecutor, data, still);
    }

    protected boolean needWait0(SprayVariableContainer variables, SprayProcessStepExecutor fromExecutor, SprayData data, boolean still) {
        return false;
    }


    private synchronized void initBeforeRun(SprayVariableContainer variables, SprayProcessStepExecutor fromExecutor, SprayData data, boolean still) {
        Thread.currentThread().setContextClassLoader(this.getClassLoader());
        MDC.put("transactionId", this.getCoordinator().getMeta().transactionId());
        MDC.put("executorId", this.stepMeta.getExecutorNameKey(this.getCoordinator().getMeta()));
        MDC.put("tid", String.valueOf(Thread.currentThread().getId()));
        initBeforeRun0(variables, fromExecutor, data, still);
    }
    protected synchronized void initBeforeRun0(SprayVariableContainer variables, SprayProcessStepExecutor fromExecutor, SprayData data, boolean still) {}

    /**
     * △! make sure you know what the 'doExecute' method do if you want to overwrite this method !!!
     * an execution method which won't throw exception
     * @param variables the variables belong to current executor
     * @param fromExecutor the last executor
     * @param data data published by the last executor
     * @param still does it still have data to publish
     */
    @Override
    public void execute(SprayVariableContainer variables, SprayProcessStepExecutor fromExecutor, SprayData data, boolean still) {
        if (this.getThreadPoll() != null) {
            this.getThreadPoll().submit(
                    () -> doExecute(variables, fromExecutor, data, still));
        } else {
            doExecute(variables, fromExecutor, data, still);
        }
    }

    private void doExecute(SprayVariableContainer variables, SprayProcessStepExecutor fromExecutor, SprayData data, boolean still) {
        try {
            this.getStepResult().incrementRunningCount();
            initBeforeRun(variables, fromExecutor, data, still);
            this._execute(variables, fromExecutor, data, still);
        } catch (Throwable e) {
            throw new SprayExecuteException(this.getMeta(), e);
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
    protected final void waitUntilBeforeExecutionDone() {
        while (this.runningCount() > 0) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new SprayExecuteException(this.getMeta(), e);
            }
        }
        if (Thread.currentThread().isInterrupted()) {
            throw new SprayExecuteException(this.getMeta(), new InterruptedException());
        }
    }

    @Override
    public final void close() throws Exception {
        waitUntilBeforeExecutionDone();
        try {
            this.getThreadPoll().shutdown();
        } catch (Exception e) {
            // TODO Handle with handler
            throw e;
        }
        this.getClassLoader().close();
        destroy();
    }
    protected void destroy() throws Exception {

    }
}
