package top.spray.engine.step.executor;

import top.spray.core.engine.execute.SprayMetaDrive;
import top.spray.core.engine.props.SprayData;
import top.spray.engine.coordinate.coordinator.SprayProcessCoordinator;
import top.spray.engine.step.instance.SprayStepResultInstance;
import top.spray.engine.step.meta.SprayProcessStepMeta;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

/**
 * Define the executor of a process node
 */
public interface SprayProcessStepExecutor extends SprayMetaDrive<SprayProcessStepMeta>, Runnable {
    static <T extends SprayProcessStepMeta> SprayProcessStepExecutor create(
            SprayProcessCoordinator coordinator, SprayProcessStepMeta stepMeta) {
        String executorId = coordinator.getMeta().transactionId() + "_" + stepMeta.getId();
        SprayProcessStepExecutor stepExecutor = coordinator.getExecutor(executorId);
        if (stepExecutor == null) {
            synchronized (coordinator) {
                if ((stepExecutor = coordinator.getExecutor(stepMeta.getId())) == null) {
                    try {
                        stepExecutor = stepMeta.executorClass().getConstructor().newInstance();
                        stepExecutor.setMeta(stepMeta);
                        stepExecutor.setCoordinator(coordinator);
                        stepExecutor.init();
                    } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                        throw new RuntimeException(e);
                    }
                    coordinator.registerExecutor(executorId, stepExecutor);
                }
            }
        }
        return stepExecutor;
    }
    void init();
    String executorId();
    String executorType();

    @Override
    SprayProcessStepMeta getMeta();
    SprayProcessCoordinator getCoordinator();
    void setMeta(SprayProcessStepMeta meta);
    void setCoordinator(SprayProcessCoordinator coordinator);
    default Map<String, Object> getProcessData() {
        return this.getCoordinator().getProcessData();
    }

    SprayStepResultInstance<? extends SprayProcessStepExecutor> getStepResult();

    default void publishData(SprayData data, boolean still) {
        this.getCoordinator().runNextNodes(this, data, still);
    }

    /**
     * data input like a subscribe method
     * @param fromExecutor data from executor
     * @param data  data
     * @param still if the data is still
     * @return return current executor for running
     */
    default SprayProcessStepExecutor dataInput(SprayProcessStepExecutor fromExecutor, SprayData data, boolean still) {
        // run with each data by default
        init();
        if (getStepResult().getStartTime() == 0) {
            getStepResult().synchronizedInit();
        }
        return this;
    }

    @Override
    void run();

    /** true if the executor need to run with batch data */
    default boolean needWait(SprayProcessStepExecutor fromExecutor, SprayData data, boolean still) {
        return false;
    }
}
