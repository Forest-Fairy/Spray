package top.spray.engine.event.model.execute.step;

import top.spray.engine.coordinate.coordinator.SprayProcessCoordinator;
import top.spray.engine.event.model.SprayBaseEvent;
import top.spray.engine.event.model.SprayEvent;

public class SprayPassByEvent extends SprayBaseEvent {
    public static final String NAME = "pass_by_event";
    private final String fromExecutorNameKey;
    private final SprayEvent lastEvent;

    public SprayPassByEvent(SprayProcessCoordinator coordinator, String fromExecutorNameKey, SprayEvent lastEvent) {
        super(coordinator, NAME, System.currentTimeMillis());
        this.fromExecutorNameKey = fromExecutorNameKey;
        this.lastEvent = lastEvent;
    }

    public String getFromExecutorNameKey() {
        return fromExecutorNameKey;
    }

    public SprayEvent getLastEvent() {
        return lastEvent;
    }
}
