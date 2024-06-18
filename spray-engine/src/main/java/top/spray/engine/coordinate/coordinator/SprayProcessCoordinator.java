package top.spray.engine.coordinate.coordinator;

import top.spray.core.engine.execute.SprayMetaDrive;
import top.spray.core.engine.props.SprayData;
import top.spray.core.engine.props.SprayPoolExecutor;
import top.spray.core.engine.result.SprayCoordinateStatus;
import top.spray.engine.coordinate.meta.SprayProcessCoordinatorMeta;
import top.spray.engine.step.executor.SprayProcessStepExecutor;
import top.spray.engine.step.condition.SprayNextStepFilter;
import top.spray.engine.step.instance.SprayStepResultInstance;

import java.io.Closeable;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Define the coordinator of a process
 */
public interface SprayProcessCoordinator extends
        SprayMetaDrive<SprayProcessCoordinatorMeta>,
        Supplier<SprayCoordinateStatus>, Runnable, Closeable {
    @Override
    SprayProcessCoordinatorMeta getMeta();

    Map<String, Object> getProcessData();

    /** a method for completable future */
    @Override
    default SprayCoordinateStatus get() {
        this.run();
        // this method run after the run method so that can get the result status.
        return status();
    }
    /** the execution method */
    @Override
    void run();

    /** the coordinator's status: none-blocked method  */
    SprayCoordinateStatus status();

    /** the spray pooled executor */
    SprayPoolExecutor getSprayPoolExecutor();

    /** register the executor by this way */
    void registerExecutor(String executorId, SprayProcessStepExecutor executor);
    /** the only way to get the executor */
    SprayProcessStepExecutor getStepExecutor(String executorId);
    int createExecutorCount();

    /** a method for executor to publish its data */
    void dispatch(SprayProcessStepExecutor fromExecutor, SprayData data, boolean still, SprayNextStepFilter filter);

    /**
     * unit method for executing
     */
    SprayStepResultInstance executeNext(SprayProcessStepExecutor nextStepExecutor, SprayProcessStepExecutor fromExecutor, SprayData data, boolean still);

}
