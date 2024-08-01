package top.spray.engine.event.model.execute.error;

import top.spray.engine.coordinate.coordinator.SprayProcessCoordinator;
import top.spray.engine.event.model.SprayBaseEvent;
import top.spray.engine.event.model.SprayEvent;
import top.spray.engine.event.model.execute.SprayExecuteEvent;
import top.spray.engine.step.executor.SprayExecutorDefinition;

public class SprayConsumeFailedEvent extends SprayBaseEvent implements SprayExecuteEvent {
    public static final String NAME = "event_consume_failed";
    private final SprayExecutorDefinition executorDefinition;
    private final SprayEvent event;
    private final long eventTime;
    private final Throwable throwable;

    public SprayConsumeFailedEvent(SprayExecutorDefinition executorDefinition, SprayEvent event, Throwable throwable) {
        this(executorDefinition, event, throwable, System.currentTimeMillis());
    }
    public SprayConsumeFailedEvent(SprayExecutorDefinition executorDefinition, SprayEvent event, Throwable throwable, long eventTime) {
        super(executorDefinition.getCoordinator(), NAME, eventTime);
        this.executorDefinition = executorDefinition;
        this.event = event;
        this.eventTime = eventTime;
        this.throwable = throwable;
    }


    public SprayEvent getEvent() {
        return this.event;
    }

    @Override
    public SprayProcessCoordinator getCoordinator() {
        return executorDefinition.getCoordinator();
    }

    @Override
    public String getEventName() {
        return NAME;
    }

    @Override
    public long getEventTime() {
        return eventTime;
    }

    @Override
    public SprayExecutorDefinition getDefinition() {
        return executorDefinition;
    }

    public Throwable getThrowable() {
        return throwable;
    }
}
