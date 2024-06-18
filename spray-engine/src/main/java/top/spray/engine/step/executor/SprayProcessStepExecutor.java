package top.spray.engine.step.executor;

import top.spray.core.engine.execute.SprayMetaDrive;
import top.spray.core.engine.props.SprayData;
import top.spray.core.util.SprayClassLoader;
import top.spray.engine.coordinate.coordinator.SprayProcessCoordinator;
import top.spray.engine.step.instance.SprayStepResultInstance;
import top.spray.engine.step.meta.SprayProcessStepMeta;

import java.util.Map;

/**
 * Define the executor of a process node
 */
public interface SprayProcessStepExecutor extends SprayMetaDrive<SprayProcessStepMeta> {
    String getExecutorId();
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
    SprayStepResultInstance getStepResult();

    void execute(SprayProcessStepExecutor fromExecutor, SprayData data, boolean still);

    /** true if the executor need to run with batch data or other reason
     *  - the executor can collect data by overwrite this method
     * @return false by default, that also means the executor can run with stream data
     */
    boolean needWait(SprayProcessStepExecutor fromExecutor, SprayData data, boolean still);
}
