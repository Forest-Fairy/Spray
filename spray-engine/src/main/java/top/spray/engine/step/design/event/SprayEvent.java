package top.spray.engine.step.design.event;

import com.alibaba.fastjson2.annotation.JSONCreator;

public class SprayEvent {
    public static final String PROCESS_INIT = "init";
    public static final String DATA_INPUT = "data_execute";
    public static final String DATA_OUTPUT = "data_publish";
    public static final String EXECUTOR_DONE = "done";

    private final String eventName;
    private final long eventTime;
    private final String fromExecutorNameKey;

    private final Object[] params;

    @JSONCreator(parameterNames = {"eventName", "eventTime", "fromExecutorNameKey", "params"})
    private SprayEvent(String eventName, long eventTime, String fromExecutorNameKey, Object[] params) {
        this.eventName = eventName;
        this.eventTime = eventTime;
        this.fromExecutorNameKey = fromExecutorNameKey;
        this.params = params;
    }
    private SprayEvent(String eventName, long eventTime, String fromExecutorNameKey) {
        this.eventName = eventName;
        this.eventTime = eventTime;
        this.fromExecutorNameKey = fromExecutorNameKey;
        this.params = new Object[0];
    }

    public String getEventName() {
        return eventName;
    }

    public long getEventTime() {
        return eventTime;
    }

    public String getFromExecutorNameKey() {
        return fromExecutorNameKey;
    }

    public Object[] getParams() {
        return params;
    }

    private static ThreadLocal<SprayEvent> local_event = new ThreadLocal<>();
    public static SprayEvent get() {
        if (local_event.get() == null) {
            return _newEvent(null, null, null);
        } else {
            return local_event.get();
        }
    }
    public static SprayEvent newEvent(String fromExecutorNameKey, Object... params) {
        return _newEvent(local_event.get(), fromExecutorNameKey, params);
    }
    private static SprayEvent _newEvent(SprayEvent parentEvent, String fromExecutorNameKey, Object... params) {
        SprayEvent sprayEvent;
        if (parentEvent == null) {
            sprayEvent = new SprayEvent(PROCESS_INIT, System.currentTimeMillis(), fromExecutorNameKey, params);
        } else {
            sprayEvent = new SprayEvent(parentEvent.eventName, parentEvent.eventTime, parentEvent.fromExecutorNameKey, parentEvent.params);
        }
        local_event.set(sprayEvent);
        return sprayEvent;
    }
}
