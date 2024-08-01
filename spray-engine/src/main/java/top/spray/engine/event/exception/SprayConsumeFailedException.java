package top.spray.engine.event.exception;

import top.spray.engine.event.model.SprayEvent;
import top.spray.engine.exception.SprayExecuteException;
import top.spray.engine.step.executor.SprayExecutorDefinition;

public class SprayConsumeFailedException extends SprayExecuteException implements SprayEventException {
    private static final String message_key = "execute.consume.failed";
    private final SprayEvent event;

    public SprayConsumeFailedException(SprayExecutorDefinition executorDefinition, SprayEvent event, Throwable cause) {
        super(executorDefinition, cause);
        this.event = event;
    }
    public SprayConsumeFailedException(SprayExecutorDefinition executorDefinition, SprayEvent event, Throwable cause, String msg) {
        super(executorDefinition, cause, msg);
        this.event = event;
    }

    @Override
    public SprayEvent getEvent() {
        return event;
    }
}
