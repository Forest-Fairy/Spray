package top.spray.engine.process.processor.execute.event;


import top.spray.engine.process.infrastructure.listen.SprayListenEvent;
import top.spray.engine.process.processor.execute.step.executor.SprayStepExecutor;

public interface SprayExecuteEvent<T> extends SprayListenEvent<T> {
    SprayStepExecutor getExecutor();
}
