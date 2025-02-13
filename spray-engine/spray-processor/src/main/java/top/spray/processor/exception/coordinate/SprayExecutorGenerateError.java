package top.spray.processor.exception.coordinate;

import top.spray.processor.exception.base.SprayCoordinateException;
import top.spray.processor.process.dispatch.coordinate.coordinator.SprayProcessCoordinator;
import top.spray.processor.process.execute.step.meta.SprayProcessExecuteStepMeta;

public class SprayExecutorGenerateError extends SprayCoordinateException {
    private static final String message_key = "coordinate.executor.generate.error";
    public SprayExecutorGenerateError(SprayProcessCoordinator coordinator, SprayProcessExecuteStepMeta stepMeta, Throwable cause) {
        super(cause, message_key,
                coordinator.name(),
                String.format("%s[%s]", stepMeta.getName(), stepMeta.getId()),
                cause.getMessage());
    }
}
