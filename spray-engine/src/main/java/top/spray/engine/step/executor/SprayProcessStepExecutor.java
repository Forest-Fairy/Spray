package top.spray.engine.step.executor;

import top.spray.core.engine.execute.SprayMetaDrive;
import top.spray.core.engine.props.SprayData;
import top.spray.core.util.SprayClassLoader;
import top.spray.engine.coordinate.coordinator.SprayProcessCoordinator;
import top.spray.engine.prop.SprayVariableContainer;
import top.spray.engine.step.executor.type.SprayReaderExecutorType;
import top.spray.engine.step.instance.SprayStepResultInstance;
import top.spray.engine.step.meta.SprayProcessStepMeta;

/**
 * Define the executor of a process node
 */
public interface SprayProcessStepExecutor extends SprayMetaDrive<SprayProcessStepMeta> {
    void initOnlyAtCreate();
    long getCreateTime();

    /**
     * how many data is running with the executor
     */
    long runningCount();

    @Override
    SprayProcessStepMeta getMeta();
    SprayProcessCoordinator getCoordinator();
    SprayClassLoader getClassLoader();
    void setMeta(SprayProcessStepMeta meta);
    void setCoordinator(SprayProcessCoordinator coordinator);
    void setClassLoader(SprayClassLoader classLoader);
    SprayStepResultInstance getStepResult();

    /**
     * true if the executor need to run with batch data or other reason <br>
     * executor can collect data by overwrite this method <br>
     * @return false by default, that also means the executor can run with stream data
     */
    boolean needWait(SprayVariableContainer variables, SprayProcessStepExecutor fromExecutor, SprayData data, boolean still);

    /**
     * an execution method which won't throw exception
     * @param processData the processData belongs to current executor
     * @param fromExecutor the last executor
     * @param data data published by the last executor
     * @param still does it still have data to publish
     */
    void execute(SprayVariableContainer variables, SprayProcessStepExecutor fromExecutor, SprayData data, boolean still);


    /**
     * need auto run next nodes without data by coordinator <br>
     *  return false if the executor will publish data by itself
     */
    default boolean autoRunNext() {
        // if the executor is reader, it should run next nodes by publishing its data
        return ! (this instanceof SprayReaderExecutorType);
    }
}
