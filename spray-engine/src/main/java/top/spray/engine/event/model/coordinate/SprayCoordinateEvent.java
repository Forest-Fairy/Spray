package top.spray.engine.event.model.coordinate;

import top.spray.engine.coordinate.coordinator.SprayProcessCoordinator;
import top.spray.engine.event.model.SprayEvent;

public interface SprayCoordinateEvent extends SprayEvent {
    @Override
    SprayProcessCoordinator getCoordinator();
}
