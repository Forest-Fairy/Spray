package top.spray.engine.process.exception.coordinate;

import top.spray.engine.process.exception.base.SprayCoordinateException;
import top.spray.engine.process.processor.dispatch.coordinate.coordinator.SprayProcessCoordinator;

public class SprayCoordinatorUnsupportedException extends SprayCoordinateException {
    public SprayCoordinatorUnsupportedException(SprayProcessCoordinator coordinator) {
        super("coordinator.unsupported", coordinator.getClass().getName(), Thread.currentThread().getStackTrace()[1].getMethodName());
    }
}
