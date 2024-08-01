package top.spray.engine.event.model.execute;

import top.spray.engine.event.model.SprayEvent;
import top.spray.engine.step.executor.SprayExecutorDefinition;

public interface SprayExecuteEvent extends SprayEvent {
    SprayExecutorDefinition getDefinition();
}
