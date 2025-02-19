package top.spray.engine.process.infrastructure.listen;


public interface SprayListener {
    /**
     * use when wrapping listener for listenable object
     * it can help reduce many times for notifying listeners
     */
    boolean isForListenable(SprayListenable listenable);

    /**
     * notify listener when the listenable object shutdown
     */
    void whenListenableShutdown(SprayListenable listenable);

    /**
     * return false if event is abandoned
     */
    SprayListenEventReceiveResult receive(SprayListenEvent<?> event);

}
