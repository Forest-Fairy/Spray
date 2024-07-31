package top.spray.engine.event.handler;

import top.spray.engine.coordinate.coordinator.SprayProcessCoordinator;
import top.spray.engine.event.model.SprayEvent;
public interface SprayCoordinatorEventHandler extends SprayEventHandler<SprayProcessCoordinator> {
    boolean support(SprayEvent event);
    void handle(SprayEvent event, SprayProcessCoordinator coordinator);
}
