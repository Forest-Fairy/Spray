package top.spray.engine.step.executor;

import top.spray.engine.base.props.SprayData;
import top.spray.engine.coordinate.coordinator.SprayProcessCoordinator;
import top.spray.engine.step.instance.SprayStepResultInstance;
import top.spray.engine.step.meta.SprayProcessStepMeta;

import java.util.Optional;

/**
 * Define the executor of a process node
 */
public interface SprayProcessStepExecutor {
    String executorId();
    String executorType();

    /**
     * execute entry
     * @param dispatcher the dispatcher who execute this step
     * @param meta the step meta to execute with
     * @param processData process data is the default variables that combines run-time variables.
     * @param dataflow the dataflow in the process
     * @return a result instance.
     */
    SprayStepResultInstance<? extends SprayProcessStepExecutor> execute(
            SprayProcessCoordinator dispatcher,
            SprayProcessStepMeta meta,
            SprayData processData,
            Optional<SprayData> dataflow);

}
