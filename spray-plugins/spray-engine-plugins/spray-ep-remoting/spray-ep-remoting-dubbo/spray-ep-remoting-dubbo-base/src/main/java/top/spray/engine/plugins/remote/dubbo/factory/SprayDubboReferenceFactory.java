package top.spray.engine.plugins.remote.dubbo.factory;

import top.spray.engine.coordinate.coordinator.SprayProcessCoordinator;
import top.spray.engine.coordinate.handler.listen.SprayCoordinatorCreateListener;
import top.spray.engine.coordinate.handler.listen.SprayCoordinatorShutdownListener;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SprayDubboReferenceFactory {
    private static final Map<String, SprayProcessCoordinator> running_coordinators =
            new ConcurrentHashMap<>();

    public static SprayProcessCoordinator getCoordinator(String transactionId) {
        return running_coordinators.get(transactionId);
    }


    public static class SprayDubboCoordinatorCreateListener
            implements SprayCoordinatorCreateListener {
        @Override
        public void onCreate(SprayProcessCoordinator coordinator) {
            running_coordinators.put(coordinator.getMeta().transactionId(), coordinator);
        }
    }


    public static class SprayDubboCoordinatorShutdownListener
            implements SprayCoordinatorShutdownListener {
        @Override
        public void onShutdown(SprayProcessCoordinator coordinator) {
            running_coordinators.remove(coordinator.getMeta().transactionId());
        }
    }
}
