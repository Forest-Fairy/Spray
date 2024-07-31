package top.spray.engine.event.model;


public interface SprayEventReceiver {

    void receive(SprayEvent event);

    void stop();

}
