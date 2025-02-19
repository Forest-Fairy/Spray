package top.spray.engine.process.processor.execute.exception;

import top.spray.engine.process.processor.execute.step.executor.SprayStepExecutor;
import top.spray.engine.process.processor.execute.step.executor.facade.SprayStepFacade;

public class SprayStepExecuteError extends SprayExecutionException {
    private static final String messageKey = "executor.execute.error";
    public SprayStepExecuteError(SprayStepExecutor executor, Throwable cause) {
        super(cause, messageKey, executor.executorNameKey(), cause.getMessage());
    }
    public SprayStepExecuteError(SprayStepFacade stepFacade, Throwable cause) {
        super(cause, messageKey, stepFacade.executorNameKey(), cause.getMessage());
    }

    public SprayStepExecuteError(Throwable cause, String messageKey, Object... params) {
        super(cause, messageKey, params);
    }

    public SprayStepExecuteError(String messageKey, Object... params) {
        super(messageKey, params);
    }
}
