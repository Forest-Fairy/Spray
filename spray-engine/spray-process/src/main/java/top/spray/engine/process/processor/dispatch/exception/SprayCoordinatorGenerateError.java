package top.spray.engine.process.processor.dispatch.exception;

import top.spray.engine.process.processor.dispatch.coordinate.meta.SprayProcessCoordinatorMeta;

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
