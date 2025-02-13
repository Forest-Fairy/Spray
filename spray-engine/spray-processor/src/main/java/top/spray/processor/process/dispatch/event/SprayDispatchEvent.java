package top.spray.processor.process.dispatch.event;


import top.spray.processor.infrustructure.listen.SprayListenEvent;
import top.spray.processor.process.dispatch.coordinate.coordinator.SprayProcessCoordinator;

public interface SprayDispatchEvent<T> extends SprayListenEvent<T> {
    String transactionId();
    /** null if trigger is not executor */
    String from();
}
