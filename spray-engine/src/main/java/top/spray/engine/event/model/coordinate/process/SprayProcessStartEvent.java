package top.spray.engine.event.model.coordinate.process;

import top.spray.engine.coordinate.coordinator.SprayProcessCoordinator;
import top.spray.engine.event.model.SprayBaseEvent;

public class SprayProcessStartEvent extends SprayBaseEvent {
    public static final String NAME = "process_init";

    public SprayProcessStartEvent(SprayProcessCoordinator coordinator) {
        super(coordinator, NAME, System.currentTimeMillis());
    }

}
