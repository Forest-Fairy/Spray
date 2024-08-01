package top.spray.engine.event.model.execute.step;

import top.spray.engine.coordinate.coordinator.SprayProcessCoordinator;
import top.spray.engine.event.model.SprayBaseEvent;
import top.spray.engine.event.model.SprayEvent;
import top.spray.engine.event.model.execute.SprayExecuteEvent;
import top.spray.engine.step.executor.SprayExecutorDefinition;

public class SprayPassByEvent extends SprayBaseEvent implements SprayExecuteEvent {
    public static final String NAME = "event_pass_by";
    private final SprayExecutorDefinition executorDefinition;
    private final SprayEvent lastEvent;

    public SprayPassByEvent(SprayExecutorDefinition executorDefinition, SprayEvent lastEvent) {
        super(executorDefinition.getCoordinator(), NAME, System.currentTimeMillis());
        this.executorDefinition = executorDefinition;
        this.lastEvent = lastEvent;
    }
    public SprayEvent getOriginalEvent() {
        if (lastEvent instanceof SprayPassByEvent passByEvent) {
            return passByEvent.getOriginalEvent();
        }
        return lastEvent;
    }

    @Override
    public SprayExecutorDefinition getDefinition() {
        return executorDefinition;
    }

    public SprayEvent getLastEvent() {
        return lastEvent;
    }
}
