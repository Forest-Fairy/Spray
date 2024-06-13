package top.spray.engine.step.executor;

import org.slf4j.MDC;
import top.spray.core.engine.props.SprayData;
import top.spray.core.util.SprayClassLoader;
import top.spray.engine.coordinate.coordinator.SprayProcessCoordinator;
import top.spray.engine.step.executor.factory.SprayExecutorFactory;
import top.spray.engine.step.instance.SprayStepResultInstance;
import top.spray.engine.step.meta.SprayProcessStepMeta;

import java.util.HashMap;
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
    private Map<String, Object> processData;

    @Override
    public String getExecutorId() {
        return this.executorId;
    }

    @Override
    public void initOnlyAtCreate() {
        this.executorId = SprayExecutorFactory.getExecutorId(this.getCoordinator(), this.getMeta());
        this.stepResult = xxx;
        switch (this.getMeta().varCopy()) {
            case 1: {
                this.processData = new HashMap<>(this.getCoordinator().getProcessData());
            } break;
            case 2: {
                this.processData = new HashMap<>(SprayData.deepCopy(this.getCoordinator().getProcessData()))
            } break;
            default: {
                this.processData = this.getCoordinator().getProcessData();
            } break;
        }
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
    @Override
    public Map<String, Object> getProcessData() {
        return this.processData;
    }
    public SprayStepResultInstance<? extends BaseSprayProcessStepExecutor> getStepResult() {
        // create when init;
        return this.stepResult;
    }

    protected void publishData(SprayData data, boolean still) {
        this.getCoordinator().dispatch(this, data, still);
    }
    @Override
    public void run() {
        initBeforeRun();
        this.execute();
    }

    protected void initBeforeRun() {
        MDC.put("transactionId", this.getCoordinator().getMeta().transactionId());
        MDC.put("executorId", this.getExecutorId());
    }

    protected abstract void execute();
}
