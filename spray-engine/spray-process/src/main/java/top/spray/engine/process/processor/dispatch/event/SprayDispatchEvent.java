package top.spray.engine.process.processor.dispatch.event;


import top.spray.engine.process.infrastructure.listen.SprayListenEvent;

public interface SprayDispatchEvent<T> extends SprayListenEvent<T> {
    String transactionId();
    /** null if trigger is not executor */
    String from();
}
