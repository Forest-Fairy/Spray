package top.spray.engine.coordinate.handler.listen;

import top.spray.core.engine.handler.listen.SprayListener;
import top.spray.core.util.SprayServiceUtil;
import top.spray.engine.coordinate.coordinator.SprayProcessCoordinator;

public interface SprayCoordinatorCreateListener extends SprayListener {
    static void create(SprayProcessCoordinator coordinator) {
        SprayServiceUtil.loadServiceClassNameMapCache(SprayCoordinatorCreateListener.class)
                .values().forEach(listener -> listener.onCreate(coordinator));
    }
    void onCreate(SprayProcessCoordinator coordinator);
}
