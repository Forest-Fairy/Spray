package top.spray.engine.step.executor;

import top.spray.core.engine.execute.SprayMetaDrive;
import top.spray.core.engine.props.SprayData;
import top.spray.core.thread.SprayPoolExecutor;
import top.spray.core.util.SprayClassLoader;
import top.spray.engine.coordinate.coordinator.SprayProcessCoordinator;
import top.spray.engine.prop.SprayVariableContainer;
import top.spray.engine.step.instance.SprayStepResultInstance;
import top.spray.engine.step.meta.SprayProcessStepMeta;

/**
 * Define the executor of a process node
 */
public interface SprayProcessStepExecutor extends SprayMetaDrive<SprayProcessStepMeta>, AutoCloseable {
    default String getExecutorNameKey() {
        return getCoordinator().getExecutorNameKey(this);
    }
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
     * @param variables the variables belong to current executor
     * @param fromExecutor the last executor
     * @param data data published by the last executor
     * @param still does it still have data to publish
     */
    void execute(SprayVariableContainer variables, SprayProcessStepExecutor fromExecutor, SprayData data, boolean still);

    /**
     * @return
     *  0 -> no <br>
     *  1 -> easy <br>
     *  2 -> deep
     */
    int varCopyMode();

    SprayPoolExecutor getThreadPoll();

}
