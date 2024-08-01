package top.spray.engine.event.handler;


import top.spray.engine.event.model.SprayEvent;

public interface SprayEventReceiver {

    void receive(SprayEvent event);

    void stop();

}
