package top.spray.engine.coordinate.coordinator;

import top.spray.core.engine.execute.SprayMetaDrive;
import top.spray.core.engine.props.SprayData;
import top.spray.core.thread.SprayPoolExecutor;
import top.spray.core.engine.status.impl.SprayCoordinateStatus;
import top.spray.engine.coordinate.meta.SprayProcessCoordinatorMeta;
import top.spray.engine.factory.SprayExecutorFactory;
import top.spray.engine.prop.SprayVariableContainer;
import top.spray.engine.step.executor.SprayProcessStepExecutor;
import top.spray.engine.step.condition.SprayNextStepFilter;
import top.spray.engine.step.meta.SprayProcessStepMeta;

import java.util.function.Supplier;

/**
 * Define the coordinator of a process
 */
public interface SprayProcessCoordinator extends
        SprayMetaDrive<SprayProcessCoordinatorMeta>,
        Supplier<SprayCoordinateStatus>, Runnable, AutoCloseable {
    @Override
    SprayProcessCoordinatorMeta getMeta();

    /** a method for completable future */
    @Override
    default SprayCoordinateStatus get() {
        this.run();
        // this method run after the run method so that can get the status status.
        return status();
    }
    /** the execution method */
    @Override
    void run();

    /** the coordinator's status: none-blocked method  */
    SprayCoordinateStatus status();

    /** the spray pooled executor */
    SprayPoolExecutor getSprayPoolExecutor();


    default String getExecutorNameKey(SprayProcessStepMeta executorMeta) {
        return SprayExecutorFactory.getExecutorNameKey(this, executorMeta);
    }
    /** register the executor by this way */
    void registerExecutor(String executorId, SprayProcessStepExecutor executor);
    /** the only way to get the executor */
    SprayProcessStepExecutor getStepExecutor(String executorId);
    int createExecutorCount();

    /** a method for executor to publish its data */
    void dispatch(SprayVariableContainer variables, SprayProcessStepExecutor fromExecutor, SprayNextStepFilter stepFilter, SprayData data, boolean still, boolean dispatchAsync);

    /**
     * unit method for executing
     */
    void executeNext(SprayProcessStepExecutor nextStepExecutor, SprayVariableContainer lastVariables, SprayProcessStepExecutor fromExecutor, SprayData data, boolean still);

}
