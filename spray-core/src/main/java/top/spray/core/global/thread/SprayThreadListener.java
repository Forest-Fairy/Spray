package top.spray.core.global.thread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.spray.common.bean.SprayServiceUtil;
import top.spray.common.tools.SprayExceptionUtil;

import java.util.HashSet;
import java.util.Set;

public interface SprayThreadListener {
    class CompanionObject { private static final Logger log = LoggerFactory.getLogger(SprayThreadListener.class);}
    Set<SprayThreadListener> Listeners = new HashSet<>();
    static void StartOutsideThread(SprayThread thread) {
        for (SprayThreadListener listener : SprayServiceUtil.loadServiceClassNameMapCache(SprayThreadListener.class).values()) {
            try {
                listener.startOutsideThread(thread);
            } catch (Exception e) {
                CompanionObject.log.error(SprayExceptionUtil.getDetailTraceMessage(
                        new Exception("thread listener error: failed to listen thread start outside", e)));
            }
        }
        for (SprayThreadListener listener : Listeners) {
            try {
                listener.startOutsideThread(thread);
            } catch (Exception e) {
                CompanionObject.log.error(SprayExceptionUtil.getDetailTraceMessage(
                        new Exception("thread listener error: failed to listen thread start outside", e)));
            }
        }
    }
    static void StartInsideThread(SprayThread thread) {
        for (SprayThreadListener listener : SprayServiceUtil.loadServiceClassNameMapCache(SprayThreadListener.class).values()) {
            try {
                listener.startInsideThread(thread);
            } catch (Exception e) {
                CompanionObject.log.error(SprayExceptionUtil.getDetailTraceMessage(
                        new Exception("thread listener error: failed to listen thread start inside", e)));
            }
        }
        for (SprayThreadListener listener : Listeners) {
            try {
                listener.startInsideThread(thread);
            } catch (Exception e) {
                CompanionObject.log.error(SprayExceptionUtil.getDetailTraceMessage(
                        new Exception("thread listener error: failed to listen thread start inside", e)));
            }
        }
    }
    static void StopInsideThread(SprayThread thread) {
        for (SprayThreadListener listener : SprayServiceUtil.loadServiceClassNameMapCache(SprayThreadListener.class).values()) {
            try {
                listener.stopInsideThread(thread);
            } catch (Exception e) {
                CompanionObject.log.error(SprayExceptionUtil.getDetailTraceMessage(
                        new Exception("thread listener error: failed to listen thread stop inside", e)));
            }
        }
        for (SprayThreadListener listener : Listeners) {
            try {
                listener.stopInsideThread(thread);
            } catch (Exception e) {
                CompanionObject.log.error(SprayExceptionUtil.getDetailTraceMessage(
                        new Exception("thread listener error: failed to listen thread stop inside", e)));
            }
        }
    }
    static void StopOutsideThread(SprayThread thread) {
        for (SprayThreadListener listener : SprayServiceUtil.loadServiceClassNameMapCache(SprayThreadListener.class).values()) {
            try {
                listener.stopOutsideThread(thread);
            } catch (Exception e) {
                CompanionObject.log.error(SprayExceptionUtil.getDetailTraceMessage(
                        new Exception("thread listener error: failed to listen thread stop outside", e)));
            }
        }
        for (SprayThreadListener listener : Listeners) {
            try {
                listener.stopOutsideThread(thread);
            } catch (Exception e) {
                CompanionObject.log.error(SprayExceptionUtil.getDetailTraceMessage(
                        new Exception("thread listener error: failed to listen thread stop outside", e)));
            }
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
