package top.spray.engine.remote.dubbo.api;

import top.spray.engine.coordinate.coordinator.SprayProcessCoordinator;
import top.spray.engine.remote.dubbo.api.holder.SprayDubboCoordinatorReferenceHolder;

/** this is a coordinator reference */
public interface SprayDubboCoordinator extends SprayProcessCoordinator, SprayDubboCoordinatorReferenceHolder {
}
