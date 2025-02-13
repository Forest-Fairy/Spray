package top.spray.processor.infrustructure.listen;

import java.util.List;

public interface SprayListenable {
    List<SprayListener> listListeners();
    /**
     * register a listener
     * @param listener listener
     * @return true if listener is registered
     */
    boolean listenerRegister(SprayListener listener);
}
