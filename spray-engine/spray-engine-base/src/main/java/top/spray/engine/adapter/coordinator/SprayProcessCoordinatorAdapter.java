package top.spray.engine.adapter.coordinator;


import top.spray.engine.coordinate.coordinator.SprayProcessCoordinator;
import top.spray.engine.step.executor.SprayProcessStepExecutor;

public interface SprayProcessCoordinatorAdapter extends SprayProcessCoordinator {
    SprayProcessCoordinator getActualCoordinator();
}
