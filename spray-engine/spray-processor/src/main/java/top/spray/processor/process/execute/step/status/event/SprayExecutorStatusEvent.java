package top.spray.processor.process.execute.step.status.event;

import top.spray.processor.process.execute.event.SprayExecuteEvent;
import top.spray.processor.process.execute.step.status.SprayStepStatusInstance;

public interface SprayExecutorStatusEvent<Source> extends SprayExecuteEvent<Source> {
    SprayStepStatusInstance getStatusInstance();
}
