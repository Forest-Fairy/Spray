package top.spray.engine.event.model.coordinate.dispatch;

import top.spray.core.engine.types.data.dispatch.result.SprayEventDispatchResultStatus;
import top.spray.engine.coordinate.coordinator.SprayProcessCoordinator;
import top.spray.engine.event.model.SprayBaseEvent;
import top.spray.engine.event.model.SprayEvent;
import top.spray.engine.event.model.coordinate.SprayCoordinateEvent;
import top.spray.engine.event.model.execute.SprayExecuteEvent;

public abstract class SprayEventDispatchResultEvent extends SprayBaseEvent implements SprayCoordinateEvent {
    public static final String NAME = "event_dispatch_result";
    private final SprayEvent executeEvent;
    private final SprayEventDispatchResultStatus eventDispatchResultStatus;

    public SprayEventDispatchResultEvent(SprayProcessCoordinator coordinator, SprayExecuteEvent executeEvent,
                                         SprayEventDispatchResultStatus eventDispatchResultStatus) {
        super(coordinator, NAME, System.currentTimeMillis());
        this.executeEvent = executeEvent;
        this.eventDispatchResultStatus = eventDispatchResultStatus;
    }

    public SprayEvent getExecuteEvent() {
        return executeEvent;
    }

    public SprayEventDispatchResultStatus getEventDispatchResultStatus() {
        return eventDispatchResultStatus;
    }
}
