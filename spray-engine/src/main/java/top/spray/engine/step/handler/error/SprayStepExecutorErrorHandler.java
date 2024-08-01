package top.spray.engine.step.handler.error;

import top.spray.core.engine.factory.SprayErrorHandlerFactory;
import top.spray.core.engine.handler.error.SprayErrorHandler;
import top.spray.engine.step.executor.SprayExecutorDefinition;
import top.spray.engine.step.handler.SprayExecutorHandler;

public abstract class SprayStepExecutorErrorHandler implements SprayErrorHandler<SprayExecutorDefinition>, SprayExecutorHandler {
    protected SprayStepExecutorErrorHandler() {
        SprayErrorHandlerFactory.register(this);
    }

    @Override
    public abstract boolean canHandle(SprayExecutorDefinition sprayProcessStepExecutor, Throwable throwable, Object[] args);
    @Override
    public void handle(SprayExecutorDefinition executor, Throwable error, Object... args) {
        if (this.canHandle(executor, error, args)) {
            this.handle0(executor, error, args);
        }
    }
    protected abstract void handle0(SprayExecutorDefinition executor, Throwable error, Object[] args);

}
