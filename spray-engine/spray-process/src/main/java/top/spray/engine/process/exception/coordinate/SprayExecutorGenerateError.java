package top.spray.engine.process.exception.coordinate;

import top.spray.engine.process.exception.base.SprayCoordinateException;
import top.spray.engine.process.processor.dispatch.coordinate.coordinator.SprayProcessCoordinator;
import top.spray.engine.process.processor.execute.step.meta.SprayProcessExecuteStepMeta;

public class SprayExecutorGenerateError extends SprayCoordinateException {
    private static final String message_key = "coordinate.executor.generate.error";
    public SprayExecutorGenerateError(SprayProcessCoordinator coordinator, SprayProcessExecuteStepMeta stepMeta, Throwable cause) {
        super(cause, message_key,
                coordinator.name(),
                String.format("%s[%s]", stepMeta.getName(), stepMeta.getId()),
                cause.getMessage());
    }
}
