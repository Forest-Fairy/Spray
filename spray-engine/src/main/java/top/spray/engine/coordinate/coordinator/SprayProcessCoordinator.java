package top.spray.engine.coordinate.coordinator;

import top.spray.core.engine.execute.SprayMetaDrive;
import top.spray.core.engine.props.SprayData;
import top.spray.core.engine.result.SprayCoordinateStatus;
import top.spray.engine.coordinate.meta.SprayProcessCoordinatorMeta;
import top.spray.engine.step.executor.SprayProcessStepExecutor;
import top.spray.engine.step.instance.SprayStepResultInstance;
import top.spray.engine.step.meta.SprayProcessStepMeta;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.function.Supplier;

/**
 * Define the coordinator of a process
 */
public interface SprayProcessCoordinator extends SprayMetaDrive<SprayProcessCoordinatorMeta>, Supplier<SprayCoordinateStatus>, Runnable {
    @Override
    SprayProcessCoordinatorMeta getMeta();

    @Override
    default SprayCoordinateStatus get() {
        this.run();
        return status();
    }
    @Override
    void run();

    boolean isDone();
    SprayCoordinateStatus status();


    Executor getThreadExecutor();

    void registerExecutor(String executorId, SprayProcessStepExecutor executor);
    SprayProcessStepExecutor getThreadExecutor(String executorId);


    void dispatch(List<SprayProcessStepMeta> nodes, SprayProcessStepExecutor fromExecutor, SprayData data, boolean still);

    void finish(SprayProcessStepExecutor sprayProcessStepExecutor);

    Map<String, Object> getProcessData();

    void beforeExecute(SprayProcessStepExecutor executor);

    SprayStepResultInstance<?> execute(SprayProcessStepExecutor executor);

    void postExecute(SprayProcessStepExecutor executor);
}
