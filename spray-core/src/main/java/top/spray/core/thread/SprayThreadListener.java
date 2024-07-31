package top.spray.core.thread;

import top.spray.core.util.SprayServiceUtil;

import java.util.HashSet;
import java.util.Set;

public interface SprayThreadListener {
    Set<SprayThreadListener> Listeners = new HashSet<>();
    static void StartOutsideThread(SprayThread thread) {
        for (SprayThreadListener listener : SprayServiceUtil.loadServiceClassNameMapCache(SprayThreadListener.class).values()) {
            try {
                listener.startOutsideThread(thread);
            } catch (Exception ignored) {}
        }
        for (SprayThreadListener listener : Listeners) {
            try {
            listener.startOutsideThread(thread);
        } catch (Exception ignored) {}
        }
    }
    static void StartInsideThread(SprayThread thread) {
        for (SprayThreadListener listener : SprayServiceUtil.loadServiceClassNameMapCache(SprayThreadListener.class).values()) {
            try {
                listener.startInsideThread(thread);
            } catch (Exception ignored) {}
        }
        for (SprayThreadListener listener : Listeners) {
            try {
                listener.startInsideThread(thread);
            } catch (Exception ignored) {}
        }
    }
    static void StopInsideThread(SprayThread thread) {
        for (SprayThreadListener listener : SprayServiceUtil.loadServiceClassNameMapCache(SprayThreadListener.class).values()) {
            try {
                listener.stopInsideThread(thread);
            } catch (Exception ignored) {}
        }
        for (SprayThreadListener listener : Listeners) {
            try {
                listener.stopInsideThread(thread);
            } catch (Exception ignored) {}
        }
    }
    static void StopOutsideThread(SprayThread thread) {
        for (SprayThreadListener listener : SprayServiceUtil.loadServiceClassNameMapCache(SprayThreadListener.class).values()) {
            try {
                listener.stopOutsideThread(thread);
            } catch (Exception ignored) {}
        }
        for (SprayThreadListener listener : Listeners) {
            try {
                listener.stopOutsideThread(thread);
            } catch (Exception ignored) {}
        }
    }


    static void register(SprayThreadListener listener) {
        Listeners.add(listener);
    }

    default void startOutsideThread(SprayThread thread) {}
    default void startInsideThread(SprayThread thread) {}
    default void stopInsideThread(SprayThread thread) {}
    default void stopOutsideThread(SprayThread thread) {}
}
