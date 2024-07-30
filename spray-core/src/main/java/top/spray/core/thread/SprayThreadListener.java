package top.spray.core.thread;

import top.spray.core.util.SprayServiceUtil;

import java.util.HashSet;
import java.util.Set;

public interface SprayThreadListener {
    Set<SprayThreadListener> Listeners = new HashSet<>();
    static void StartOutThread(SprayThread thread) {
        for (SprayThreadListener listener : SprayServiceUtil.loadServiceClassNameMapCache(SprayThreadListener.class).values()) {
            try {
                listener.startOutThread(thread);
            } catch (Exception ignored) {}
        }
        for (SprayThreadListener listener : Listeners) {
            try {
            listener.startOutThread(thread);
        } catch (Exception ignored) {}
        }
    }
    static void StartInThread(SprayThread thread) {
        for (SprayThreadListener listener : SprayServiceUtil.loadServiceClassNameMapCache(SprayThreadListener.class).values()) {
            try {
                listener.startInThread(thread);
            } catch (Exception ignored) {}
        }
        for (SprayThreadListener listener : Listeners) {
            try {
                listener.startInThread(thread);
            } catch (Exception ignored) {}
        }
    }
    static void StopInThread(SprayThread thread) {
        for (SprayThreadListener listener : SprayServiceUtil.loadServiceClassNameMapCache(SprayThreadListener.class).values()) {
            try {
                listener.stopInThread(thread);
            } catch (Exception ignored) {}
        }
        for (SprayThreadListener listener : Listeners) {
            try {
                listener.stopInThread(thread);
            } catch (Exception ignored) {}
        }
    }
    static void StopOutThread(SprayThread thread) {
        for (SprayThreadListener listener : SprayServiceUtil.loadServiceClassNameMapCache(SprayThreadListener.class).values()) {
            try {
                listener.stopOutThread(thread);
            } catch (Exception ignored) {}
        }
        for (SprayThreadListener listener : Listeners) {
            try {
                listener.stopOutThread(thread);
            } catch (Exception ignored) {}
        }
    }


    static void register(SprayThreadListener listener) {
        Listeners.add(listener);
    }

    default void startOutThread(SprayThread thread) {}
    default void startInThread(SprayThread thread) {}
    default void stopInThread(SprayThread thread) {}
    default void stopOutThread(SprayThread thread) {}
}
