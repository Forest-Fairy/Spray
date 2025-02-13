package top.spray.processor.process.dispatch.coordinate.status.event;


import top.spray.processor.infrustructure.listen.SprayListenEvent;
import top.spray.processor.process.dispatch.coordinate.status.SprayCoordinatorStatusInstance;

public interface SprayCoordinatorStatusEvent<Source> extends SprayListenEvent<Source> {
    SprayCoordinatorStatusInstance getStatusInstance();
}
