package top.spray.engine.event.handler;

import top.spray.engine.coordinate.coordinator.SprayProcessCoordinator;
import top.spray.engine.event.model.SprayEvent;
import top.spray.engine.step.executor.SprayProcessStepExecutor;

public interface SprayExecuteEventHandler extends SprayEventHandler<SprayProcessStepExecutor> {
    boolean support(SprayEvent event);
    void handle(SprayEvent event, SprayProcessCoordinator coordinator);
}
