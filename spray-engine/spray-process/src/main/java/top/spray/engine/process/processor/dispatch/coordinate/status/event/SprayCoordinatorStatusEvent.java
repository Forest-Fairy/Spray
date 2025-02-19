package top.spray.engine.process.processor.dispatch.coordinate.status.event;


import top.spray.engine.process.infrastructure.listen.SprayListenEvent;
import top.spray.engine.process.processor.dispatch.coordinate.status.SprayCoordinatorStatusInstance;

public interface SprayCoordinatorStatusEvent<Source> extends SprayListenEvent<Source> {
    SprayCoordinatorStatusInstance getStatusInstance();
}
