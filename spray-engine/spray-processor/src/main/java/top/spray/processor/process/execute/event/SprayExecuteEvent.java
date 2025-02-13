package top.spray.processor.process.execute.event;


import top.spray.processor.infrustructure.listen.SprayListenEvent;
import top.spray.processor.process.execute.step.executor.SprayStepExecutor;

public interface SprayExecuteEvent<T> extends SprayListenEvent<T> {
    SprayStepExecutor getExecutor();
}
