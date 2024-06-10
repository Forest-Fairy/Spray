package top.spray.engine.step.executor;

import top.spray.engine.base.execute.SprayMetaDrive;
import top.spray.engine.base.meta.SprayBaseMeta;
import top.spray.engine.base.props.SprayData;
import top.spray.engine.coordinate.coordinator.SprayProcessCoordinator;
import top.spray.engine.step.instance.SprayStepResultInstance;
import top.spray.engine.step.meta.SprayProcessStepMeta;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Define the executor of a process node
 */
public interface SprayProcessStepExecutor {
    Map<Class<? extends SprayProcessStepExecutor>, SprayProcessStepExecutor> EXECUTOR_MAP = new HashMap<>();
    static <T extends SprayProcessStepMeta> SprayProcessStepExecutor create(SprayProcessStepMeta stepMeta) {
        SprayProcessStepExecutor sprayProcessStepExecutor = EXECUTOR_MAP.get(stepMeta.executorClass());
        if (sprayProcessStepExecutor == null) {
            synchronized (EXECUTOR_MAP) {
                if ((sprayProcessStepExecutor = EXECUTOR_MAP.get(stepMeta.executorClass())) == null) {
                    try {
                        sprayProcessStepExecutor = stepMeta.executorClass().getConstructor().newInstance();
                    } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                        throw new RuntimeException(e);
                    }
                    EXECUTOR_MAP.put(stepMeta.executorClass(), sprayProcessStepExecutor);
                }
            }
        }
        return sprayProcessStepExecutor;
    }
    String executorId();
    String executorType();

    /**
     * execute entry
     * @param dispatcher the dispatcher who execute this step
     * @param meta the step meta to execute with
     * @param processData process data is the default variables that combines run-time variables.
     * @param dataflow the dataflow in the process
     * @param stepResult stepResultContainer
     * @return a result instance.
     */
    void execute(
            SprayProcessCoordinator dispatcher,
            SprayProcessStepMeta meta,
            SprayData processData,
            Stream<SprayData> dataflow,
            SprayStepResultInstance<? extends SprayProcessStepExecutor> stepResult);

}
