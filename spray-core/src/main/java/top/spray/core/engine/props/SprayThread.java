package top.spray.core.engine.props;

import org.slf4j.MDC;

import java.util.HashMap;
import java.util.Map;

public class SprayThread extends Thread {
    /** copy mdc context by default */
    private Map<String, String> parentThreadMdcContextMap;

    @Override
    public synchronized void start() {
        try {
            if (parentThreadMdcContextMap == null) {
                parentThreadMdcContextMap = MDC.getCopyOfContextMap();
            }
        } catch (Exception ignore) {}
        super.start();
    }

    public void setParentThreadMdcContextMap(Map<String, String> parentThreadMdcContextMap) {
        this.parentThreadMdcContextMap = parentThreadMdcContextMap;
    }

    @Override
    public void run() {
        try {
            MDC.setContextMap(parentThreadMdcContextMap == null ? new HashMap<>() :
                    new HashMap<>(parentThreadMdcContextMap));
        } catch (Exception ignore) {}
        super.run();
    }
}
