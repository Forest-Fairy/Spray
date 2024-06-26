package top.spray.engine.coordinate.handler.error;

import top.spray.core.engine.handler.error.SprayErrorHandler;
import top.spray.engine.coordinate.coordinator.SprayProcessCoordinator;

public interface SprayCoordinatorErrorHandler extends SprayErrorHandler<SprayProcessCoordinator> {
    @Override
    void handle(SprayProcessCoordinator sprayProcessCoordinator, Throwable error, Object... args);
}
