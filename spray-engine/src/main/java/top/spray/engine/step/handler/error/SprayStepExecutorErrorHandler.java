package top.spray.engine.step.handler.error;

import top.spray.core.engine.factory.SprayErrorHandlerFactory;
import top.spray.core.engine.handler.error.SprayErrorHandler;
import top.spray.engine.step.executor.SprayProcessStepExecutor;

public abstract class SprayStepExecutorErrorHandler implements SprayErrorHandler<SprayProcessStepExecutor> {
    protected SprayStepExecutorErrorHandler() {
        SprayErrorHandlerFactory.register(this);
    }

    @Override
    public abstract boolean canHandle(SprayProcessStepExecutor sprayProcessStepExecutor, Throwable throwable, Object args);
    @Override
    public void handle(SprayProcessStepExecutor executor, Throwable error, Object... args) {
        if (this.canHandle(executor, error, args)) {
            this.handle0(executor, error, args);
        }
    }
    protected abstract void handle0(SprayProcessStepExecutor executor, Throwable error, Object[] args);

}
