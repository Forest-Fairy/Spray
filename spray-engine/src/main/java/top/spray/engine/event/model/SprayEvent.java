package top.spray.engine.event.model;


import top.spray.engine.coordinate.coordinator.SprayProcessCoordinator;

public interface SprayEvent {
    SprayProcessCoordinator getCoordinator();
    String getEventName();
    long getEventTime();
}
