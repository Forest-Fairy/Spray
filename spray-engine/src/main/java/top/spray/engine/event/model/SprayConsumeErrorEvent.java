package top.spray.engine.event.model;

import top.spray.engine.coordinate.coordinator.SprayProcessCoordinator;

import java.lang.reflect.Method;

public class SprayConsumeErrorEvent extends SprayBaseEvent {
    public static final String NAME = "consume_error_event";
    private final SprayEventReceiver receiver;
    private final Throwable error;
    private final SprayEvent event;
    public SprayConsumeErrorEvent(SprayProcessCoordinator coordinator, SprayEventReceiver receiver, SprayEvent event, Throwable error) {
        super(coordinator, NAME, System.currentTimeMillis());
        this.receiver = receiver;
        this.event = event;
        this.error = error;
    }

    public SprayEventReceiver getReceiver() {
        return receiver;
    }

    public SprayEvent getEvent() {
        return event;
    }

    public Throwable getError() {
        return error;
    }
}
