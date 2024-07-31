package top.spray.engine.event.handler;

import top.spray.core.engine.handler.listen.SprayListener;
import top.spray.engine.event.model.SprayEvent;
import top.spray.engine.event.model.SprayEventReceiver;

public interface SprayEventHandler<T extends SprayEventReceiver> extends SprayListener {
    boolean support(SprayEvent event);
    void handle(SprayEvent event, T t);
}
