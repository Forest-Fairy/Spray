package top.spray.processor.exception.coordinate;

import top.spray.processor.exception.base.SprayCoordinateException;
import top.spray.processor.exception.base.SprayEngineException;
import top.spray.processor.process.dispatch.coordinate.meta.SprayProcessCoordinatorMeta;

/**
 * spray. i18n. exception. engine. coordinate
 */
public class SprayCoordinatorGenerateError extends SprayCoordinateException {
    private static final String message_key = "coordinator.generate.error";
    public SprayCoordinatorGenerateError(String transactionId, SprayProcessCoordinatorMeta coordinatorMeta, Throwable cause) {
        super(cause, message_key, coordinatorMeta.getName(),
                String.format("[%s]%s(%s)", transactionId, coordinatorMeta.getName(), coordinatorMeta.getId()), cause.getMessage());
    }
}
