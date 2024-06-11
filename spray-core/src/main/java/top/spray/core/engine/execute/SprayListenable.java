package top.spray.core.engine.execute;

import top.spray.core.engine.handler.listen.SprayListener;

import java.util.List;

public interface SprayListenable<T extends SprayListener> {
    SprayListenable<T> addListener(T... listeners);
    List<T> getListeners();
}
