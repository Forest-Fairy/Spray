package top.spray.processor.process.execute.step.executor.event;

import top.spray.processor.process.execute.event.SprayExecuteEvent;
import top.spray.processor.process.execute.step.executor.SprayStepExecutor;

public interface SprayExecutorEvent<T> extends SprayExecuteEvent<T> {
    SprayStepExecutor getExecutor();
}
