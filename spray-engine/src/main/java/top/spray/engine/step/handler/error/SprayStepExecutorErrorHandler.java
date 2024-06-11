package top.spray.engine.step.handler.error;

import top.spray.core.engine.handler.error.SprayErrorHandler;
import top.spray.engine.step.executor.SprayProcessStepExecutor;

public interface SprayStepExecutorErrorHandler extends SprayErrorHandler<SprayProcessStepExecutor> {
    @Override
    boolean canHandle(SprayProcessStepExecutor sprayProcessStepExecutor, Throwable throwable, Object args);

    @Override
    void handle(SprayProcessStepExecutor executor, Throwable error, Object... args);
}
