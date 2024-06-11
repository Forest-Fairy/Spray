package top.spray.engine.step.executor;

import top.spray.engine.base.execute.SprayMetaDrive;
import top.spray.engine.base.props.SprayData;
import top.spray.engine.coordinate.coordinator.SprayProcessCoordinator;
import top.spray.engine.step.instance.SprayStepResultInstance;
import top.spray.engine.step.meta.SprayProcessStepMeta;

import java.lang.reflect.InvocationTargetException;

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
                    } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                        throw new RuntimeException(e);
                    }
                    coordinator.registerExecutor(executorId, stepExecutor);
                }
            }
        }
        return stepExecutor;
    }
    void synchronizedInit();
    String executorId();
    String executorType();

    @Override
    SprayProcessStepMeta getMeta();
    SprayProcessCoordinator getCoordinator();
    SprayStepResultInstance<? extends SprayProcessStepExecutor> getStepResult();

    default void publishData(SprayData data, boolean still) {
        this.getCoordinator().goToNextNodes(this, data, still);
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
        synchronizedInit();
        if (getStepResult().getStartTime() == 0) {
            getStepResult().synchronizedInit();
        }
        return this;
    }

    @Override
    void run();
}
