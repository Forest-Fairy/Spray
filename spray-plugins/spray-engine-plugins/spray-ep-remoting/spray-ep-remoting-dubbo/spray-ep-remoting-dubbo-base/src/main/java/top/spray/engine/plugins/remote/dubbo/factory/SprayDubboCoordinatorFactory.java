package top.spray.engine.plugins.remote.dubbo.factory;

import top.spray.engine.plugins.remote.dubbo.api.target.SprayDubboCoordinator;
import top.spray.engine.plugins.remote.dubbo.consumer.target.SprayDubboCoordinatorImpl;

public class SprayDubboCoordinatorFactory {
    public static SprayDubboCoordinator createSprayDubboCoordinator(
            int dubboServicePort, String transactionId, String coordinatorMeta) {
        return new SprayDubboCoordinatorImpl();
    }
}
