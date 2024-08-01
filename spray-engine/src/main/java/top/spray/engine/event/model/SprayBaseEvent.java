package top.spray.engine.event.model;

import top.spray.engine.coordinate.coordinator.SprayProcessCoordinator;

public class SprayBaseEvent implements SprayEvent {
    protected final SprayProcessCoordinator coordinator;
    protected final String eventName;
    protected final long eventTime;
    public SprayBaseEvent(SprayProcessCoordinator coordinator, String eventName, long eventTime) {
        this.coordinator = coordinator;
        this.eventName = eventName;
        this.eventTime = eventTime;
    }

    @Override
    public SprayProcessCoordinator getCoordinator() {
        return coordinator;
    }

    @Override
    public String getEventName() {
        return eventName;
    }

    @Override
    public long getEventTime() {
        return eventTime;
    }
}
