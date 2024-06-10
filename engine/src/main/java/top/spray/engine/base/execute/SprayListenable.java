package top.spray.engine.base.execute;

import top.spray.engine.base.handler.listen.SprayListener;

import java.util.List;

public interface SprayListenable<T extends SprayListener> {
    SprayListenable<T> addListener(T... listeners);
    List<T> getListeners();
}
