package top.spray.engine.step.executor;

import top.spray.core.engine.execute.SprayClosable;
import top.spray.core.engine.execute.SprayMetaDrive;
import top.spray.core.dynamic.loader.SprayClassLoader;
import top.spray.engine.coordinate.coordinator.SprayProcessCoordinator;
import top.spray.engine.event.model.SprayEvent;
import top.spray.engine.event.handler.SprayEventReceiver;
import top.spray.engine.step.instance.SprayStepResultInstance;
import top.spray.engine.step.meta.SprayProcessStepMeta;

/**
 * Define the executor of a process node
 */
public interface SprayExecutorDefinition extends
        SprayMetaDrive<SprayProcessStepMeta>,
        SprayEventReceiver, SprayClosable {
    default String getExecutorNameKey() {
        return getMeta().getExecutorNameKey(getCoordinator().getMeta());
    }
    long getCreateTime();
    void init();
    @Override
    SprayProcessStepMeta getMeta();
    SprayProcessCoordinator getCoordinator();
    SprayClassLoader getClassLoader();
    void setMeta(SprayProcessStepMeta meta);
    void setCoordinator(SprayProcessCoordinator coordinator);
    void setClassLoader(SprayClassLoader classLoader);
    SprayStepResultInstance getStepResult();

    boolean canEventPassBy(SprayEvent event);

    /**
     * the cached event count
     */
    int eventCount();

    @Override
    void receive(SprayEvent event);

    @Override
    default void stop() {
        this.closeInRuntime();
    }


}
