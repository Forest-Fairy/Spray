package top.spray.engine.coordinate.handler.listen;

import top.spray.core.engine.handler.listen.SprayListener;
import top.spray.core.util.SprayServiceUtil;
import top.spray.engine.coordinate.coordinator.SprayProcessCoordinator;

public interface SprayCoordinatorShutdownListener extends SprayListener {
    static void shutDown(SprayProcessCoordinator coordinator) {
        SprayServiceUtil.loadServiceClassNameMapCache(SprayCoordinatorShutdownListener.class)
                .values().forEach(listener -> listener.onShutdown(coordinator));
    }
    void onShutdown(SprayProcessCoordinator coordinator);
}
