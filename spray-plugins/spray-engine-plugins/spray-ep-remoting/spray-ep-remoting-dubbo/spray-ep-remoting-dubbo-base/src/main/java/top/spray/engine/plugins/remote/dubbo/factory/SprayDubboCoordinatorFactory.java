package top.spray.engine.plugins.remote.dubbo.factory;

import top.spray.engine.plugins.remote.dubbo.api.target.SprayDubboCoordinator;
import top.spray.engine.plugins.remote.dubbo.provider.target.SprayDubboProcessCoordinator;

public class SprayDubboCoordinatorFactory {
    public static SprayDubboCoordinator createSprayDubboCoordinator(
            String transactionId, String coordinatorMeta) {
        return new SprayDubboProcessCoordinator();
    }
}
