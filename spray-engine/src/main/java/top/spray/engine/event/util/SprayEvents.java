package top.spray.engine.event.util;


import top.spray.core.thread.SprayThread;
import top.spray.core.thread.SprayThreadListener;
import top.spray.engine.event.model.SprayEvent;

public final class SprayEvents {

    private SprayEvents() {}
    private static final ThreadLocal<SprayEvent> local_event = new ThreadLocal<>();

    public static SprayEvent get() {
        return local_event.get();
    }
    public static void set(SprayEvent event) {
        local_event.set(event);
    }
    public static void remove() {
        local_event.remove();
    }

    static {
        new ThreadListener();
    }
    private static class ThreadListener implements SprayThreadListener {
        ThreadListener() {
            SprayThreadListener.register(this);
        }

        @Override
        public void stopInsideThread(SprayThread thread) {
            SprayEvents.remove();
        }
    }

}
