package top.spray.core.global.thread;

import org.slf4j.MDC;

import java.util.HashMap;
import java.util.Map;

public class SprayThread extends Thread {
    /** copy mdc context by default */
    private Map<String, String> parentThreadMdcContextMap;

    public SprayThread() {
        super();
        this.setName("spray-" + this.getName());
    }

    public SprayThread(Runnable target) {
        super(target);
        this.setName("spray-" + this.getName());
    }

    public SprayThread(ThreadGroup group, Runnable target) {
        super(group, target);
        this.setName("spray-" + this.getName());
    }

    public SprayThread(String name) {
        super(name);
    }

    public SprayThread(ThreadGroup group, String name) {
        super(group, name);
    }

    public SprayThread(Runnable target, String name) {
        super(target, name);
    }

    public SprayThread(ThreadGroup group, Runnable target, String name) {
        super(group, target, name);
    }

    public SprayThread(ThreadGroup group, Runnable target, String name, long stackSize) {
        super(group, target, name, stackSize);
    }

    public SprayThread(ThreadGroup group, Runnable target, String name, long stackSize, boolean inheritThreadLocals) {
        super(group, target, name, stackSize, inheritThreadLocals);
    }

    @Override
    public synchronized void start() {
        try {
            // this method is executed with parent thread so try to set mdc and classloader
            parentThreadMdcContextMap = MDC.getCopyOfContextMap();
            this.setContextClassLoader(Thread.currentThread().getContextClassLoader());
        } catch (Exception ignore) {}
        // the super will execute run method with new thread
        SprayThreadListener.StartOutsideThread(this);
        super.start();
        SprayThreadListener.StopOutsideThread(this);
    }

    @Override
    public void run() {
        try {
            MDC.setContextMap(parentThreadMdcContextMap == null ? new HashMap<>() :
                    new HashMap<>(parentThreadMdcContextMap));
            // remove the reference to avoid memory leak
            parentThreadMdcContextMap = null;
        } catch (Exception ignore) {}
        SprayThreadListener.StartInsideThread(this);
        try {
            super.run();
        } finally {
            SprayThreadListener.StopInsideThread(this);
        }
    }
}
