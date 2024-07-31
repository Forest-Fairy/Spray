package top.spray.engine.event.model;

import top.spray.engine.coordinate.coordinator.SprayProcessCoordinator;

public abstract class SprayBaseEvent implements SprayEvent {
    private final SprayProcessCoordinator coordinator;
    private final String eventName;
    private final long eventTime;

    public SprayBaseEvent(SprayProcessCoordinator coordinator, String eventName, long eventTime) {
        this.coordinator = coordinator;
        this.eventName = eventName.toLowerCase();
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
