package top.spray.engine.process.processor.execute.step.executor.event;

import top.spray.engine.process.processor.execute.event.SprayExecuteEvent;
import top.spray.engine.process.processor.execute.step.executor.SprayStepExecutor;

public interface SprayExecutorEvent<T> extends SprayExecuteEvent<T> {
    SprayStepExecutor getExecutor();
}
