package top.spray.engine.design.event.model;

public abstract class SprayBaseEvent implements SprayEvent {

    private final String eventName;
    private final long eventTime;

    public SprayBaseEvent(String eventName, long eventTime) {
        this.eventName = eventName.toLowerCase();
        this.eventTime = eventTime;
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
