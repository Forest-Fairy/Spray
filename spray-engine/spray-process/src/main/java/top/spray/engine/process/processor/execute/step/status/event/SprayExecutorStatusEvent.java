package top.spray.engine.process.processor.execute.step.status.event;

import top.spray.engine.process.processor.execute.event.SprayExecuteEvent;
import top.spray.engine.process.processor.execute.step.status.SprayStepStatusInstance;

public interface SprayExecutorStatusEvent<Source> extends SprayExecuteEvent<Source> {
    SprayStepStatusInstance getStatusInstance();
}
