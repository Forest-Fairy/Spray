package top.spray.engine.event.model.coordinate;

import top.spray.engine.coordinate.coordinator.SprayProcessCoordinator;
import top.spray.engine.event.model.SprayBaseEvent;
import top.spray.engine.event.model.SprayEvent;

public class SprayUnknownEvent extends SprayBaseEvent {
    public static final String NAME = "unknown_event";
    private final SprayEvent event;
    public SprayUnknownEvent(SprayProcessCoordinator coordinator, SprayEvent event) {
        super(coordinator, NAME, event.getEventTime());
        this.event = event;
    }
    public SprayEvent getEvent() {
        return this.event;
    }
}
