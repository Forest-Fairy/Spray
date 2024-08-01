package top.spray.engine.event.model.coordinate.process;

import top.spray.engine.coordinate.coordinator.SprayProcessCoordinator;
import top.spray.engine.event.model.SprayBaseEvent;
import top.spray.engine.event.model.coordinate.SprayCoordinateEvent;

public class SprayProcessStopEvent extends SprayBaseEvent implements SprayCoordinateEvent {
    public static final String NAME = "process_stop";
    public SprayProcessStopEvent(SprayProcessCoordinator coordinator) {
        super(coordinator, NAME, System.currentTimeMillis());
    }

}
