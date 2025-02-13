package top.spray.processor.exception.execute;

import top.spray.processor.exception.base.SprayExecutionException;
import top.spray.processor.process.execute.step.executor.SprayStepExecutor;
import top.spray.processor.process.execute.step.executor.facade.SprayStepFacade;

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
