package top.spray.processor.exception.coordinate;


import top.spray.processor.exception.base.SprayCoordinateException;
import top.spray.processor.process.dispatch.coordinate.coordinator.SprayProcessCoordinator;
import top.spray.processor.process.execute.step.meta.SprayOptionalData;

/**
 * spray. i18n. exception. engine. coordinate
 */
public class SprayCoordinatorDispatchErrorException extends SprayCoordinateException {
    private static final String message_key = "coordinator.dispatch.error";
    private final String fromExecutor;
    private final SprayOptionalData optionalData;
    private final SprayProcessCoordinator coordinator;

    public SprayCoordinatorDispatchErrorException(SprayProcessCoordinator coordinator, String fromExecutor, SprayOptionalData optionalData, Throwable cause) {
        super(cause, message_key);
        this.coordinator = coordinator;
        this.fromExecutor = fromExecutor;
        this.optionalData = optionalData;
    }

    @Override
    public String getMessage() {
        return toI18n(message_key, coordinator.name(), fromExecutor, optionalData.getGeneratorNameKey(), optionalData.getData().toJson());
    }
}
