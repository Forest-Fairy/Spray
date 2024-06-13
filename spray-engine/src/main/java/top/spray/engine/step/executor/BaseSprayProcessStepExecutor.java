package top.spray.engine.step.executor;

import org.slf4j.MDC;
import top.spray.core.engine.props.SprayData;
import top.spray.core.util.SprayClassLoader;
import top.spray.engine.coordinate.coordinator.SprayProcessCoordinator;
import top.spray.engine.step.executor.factory.SprayExecutorFactory;
import top.spray.engine.step.instance.SprayStepResultInstance;
import top.spray.engine.step.meta.SprayProcessStepMeta;

import java.util.Map;

/**
 * Define the executor of a process node
 */
public abstract class BaseSprayProcessStepExecutor implements SprayProcessStepExecutor {
    private String executorId;
    private SprayProcessStepMeta stepMeta;
    private SprayProcessCoordinator coordinator;
    private SprayClassLoader classLoader;
    private SprayStepResultInstance<? extends BaseSprayProcessStepExecutor> stepResult;

    public String getExecutorId() {
        return this.executorId;
    }
    public void initOnlyAtCreate() {
        this.executorId = SprayExecutorFactory.getExecutorId(this.getCoordinator(), this.getMeta());
        this.stepResult = xxx;
    }
    public void setMeta(SprayProcessStepMeta meta) {
        this.stepMeta = meta;
    }
    public void setCoordinator(SprayProcessCoordinator coordinator) {
        this.coordinator = coordinator;
    }
    public void setClassLoader(SprayClassLoader classLoader) {
        this.classLoader = classLoader;
    }
    @Override
    public SprayProcessStepMeta getMeta() {
        return this.stepMeta;
    }
    public SprayProcessCoordinator getCoordinator() {
        return this.coordinator;
    }
    public SprayClassLoader getClassLoader() {
        return this.classLoader;
    }
    public Map<String, Object> getProcessData() {
        return this.getCoordinator().getProcessData();
    }
    public SprayStepResultInstance<? extends BaseSprayProcessStepExecutor> getStepResult() {
        // create when init;
        return this.stepResult;
    }

    public void publishData(SprayData data, boolean still) {
        this.getCoordinator().dispatch(
                this.getMeta().nextNodes(),
                this, data, still);
    }

    /**
     * data input like a subscribe method
     * @param fromExecutor data from executor
     * @param data  data
     * @param still if the data is still
     * @return return current executor for running
     */
    public BaseSprayProcessStepExecutor dataInput(BaseSprayProcessStepExecutor fromExecutor, SprayData data, boolean still) {
        // run with each data by default
        if (getStepResult().getStartTime() == 0) {
            getStepResult().synchronizedInit();
        }
        return this;
    }

    protected void initBeforeRun() {
        MDC.put("transactionId", this.getCoordinator().getMeta().transactionId());
        MDC.put("executorId", this.getExecutorId());
    }
    @Override
    public void run() {
        initBeforeRun();
        this.execute();
    }

    protected abstract void execute();

    /** true if the executor need to run with batch data */
    public boolean needWait(BaseSprayProcessStepExecutor fromExecutor, SprayData data, boolean still) {
        return false;
    }
}
