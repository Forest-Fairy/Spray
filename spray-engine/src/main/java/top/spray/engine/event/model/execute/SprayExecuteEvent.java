package top.spray.engine.event.model.execute;

import top.spray.engine.coordinate.coordinator.SprayProcessCoordinator;
import top.spray.engine.event.model.SprayBaseEvent;

public abstract class SprayExecuteEvent extends SprayBaseEvent {
    public final String currentExecutorNameKey;
    public SprayExecuteEvent(SprayProcessCoordinator coordinator, String currentExecutorNameKey, String eventName, long eventTime) {
        super(coordinator, eventName, eventTime);
        this.currentExecutorNameKey = currentExecutorNameKey;
    }

    public String getCurrentExecutorNameKey() {
        return currentExecutorNameKey;
    }
}
