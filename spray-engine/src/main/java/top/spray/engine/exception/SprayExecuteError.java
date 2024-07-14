package top.spray.engine.exception;

import top.spray.engine.step.executor.SprayProcessStepExecutor;

public class SprayExecuteError extends Throwable {
    public SprayExecuteError(SprayProcessStepExecutor nextStepExecutor, Throwable e) {
    }
}
