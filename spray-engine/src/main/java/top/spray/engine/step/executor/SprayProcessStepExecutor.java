package top.spray.engine.step.executor;

import top.spray.core.engine.execute.SprayMetaDrive;
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
public interface SprayProcessStepExecutor extends SprayMetaDrive<SprayProcessStepMeta>, Runnable {
    default String getExecutorId() {
        return SprayExecutorFactory.getExecutorId(this.getCoordinator(), this.getMeta());
    }
    void initOnlyAtCreate();
    @Override
    SprayProcessStepMeta getMeta();
    SprayProcessCoordinator getCoordinator();
    SprayClassLoader getClassLoader();
    void setMeta(SprayProcessStepMeta meta);
    void setCoordinator(SprayProcessCoordinator coordinator);
    void setClassLoader(SprayClassLoader classLoader);
    default Map<String, Object> getProcessData() {
        return this.getCoordinator().getProcessData();
    }
    SprayStepResultInstance<? extends SprayProcessStepExecutor> getStepResult();

    /**
     * data input like a subscribe method
     * @param fromExecutor data from executor
     * @param data  data
     * @param still if the data is still
     * @return return current executor for running
     */
    default SprayProcessStepExecutor dataInput(SprayProcessStepExecutor fromExecutor, SprayData data, boolean still) {
        // run with each data by default
        initOnlyAtCreate();
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
