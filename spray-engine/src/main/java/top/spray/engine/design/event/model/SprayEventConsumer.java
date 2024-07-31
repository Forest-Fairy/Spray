package top.spray.engine.design.event.model;


public interface SprayEventConsumer {

    void receive(SprayEvent event);

    void consume() throws InterruptedException;

}
