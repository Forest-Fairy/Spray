package top.spray.core.engine.execute;

import top.spray.core.engine.handler.listen.SprayListener;

import java.util.List;

public interface SprayListenable<E extends SprayListenable<?, ?>, T extends SprayListener> {
    E addListener(T listeners);
    List<T> getListeners();
}
