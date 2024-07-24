package top.spray.engine.remote.dubbo.api.holder;

import top.spray.engine.remote.dubbo.api.SprayDubboCoordinatorReference;
import top.spray.engine.remote.dubbo.api.SprayDubboVariablesReference;

public interface SprayDubboCoordinatorReferenceHolder {
    SprayDubboCoordinatorReference getCoordinatorReference();
}
