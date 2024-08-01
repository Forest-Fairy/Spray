package top.spray.engine.event.handler;

import top.spray.engine.coordinate.coordinator.SprayProcessCoordinator;
import top.spray.engine.event.model.SprayEvent;
import top.spray.engine.event.model.coordinate.SprayCoordinateEvent;

public interface SprayCoordinatorEventHandler {
    boolean support(SprayEvent event);
    void handle(SprayCoordinateEvent event, SprayProcessCoordinator coordinator);
}
