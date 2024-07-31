package top.spray.engine.design.event.model.coordinate;

import top.spray.engine.coordinate.coordinator.SprayProcessCoordinator;
import top.spray.engine.design.event.model.SprayBaseEvent;

public abstract class SprayCoordinateEvent extends SprayBaseEvent {
    protected final SprayProcessCoordinator coordinator;
    public SprayCoordinateEvent(SprayProcessCoordinator coordinator, String eventName, long eventTime) {
        super(eventName, eventTime);
        this.coordinator = coordinator;
    }

    public SprayProcessCoordinator getCoordinator() {
        return coordinator;
    }
}
