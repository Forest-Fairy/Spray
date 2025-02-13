package top.spray.processor.exception.coordinate;

import top.spray.processor.exception.base.SprayCoordinateException;
import top.spray.processor.process.dispatch.coordinate.coordinator.SprayProcessCoordinator;

public class SprayCoordinatorUnsupportedException extends SprayCoordinateException {
    public SprayCoordinatorUnsupportedException(SprayProcessCoordinator coordinator) {
        super("coordinator.unsupported", coordinator.getClass().getName(), Thread.currentThread().getStackTrace()[1].getMethodName());
    }
}
