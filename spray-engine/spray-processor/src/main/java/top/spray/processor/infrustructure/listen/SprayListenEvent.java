package top.spray.processor.infrustructure.listen;

import top.spray.common.tools.SprayOptional;

public interface SprayListenEvent<Source> {
    String getEventId();
    long getEventTime();
    void setAttr(String key, String val);
    String getAttr(String key);
    SprayOptional<Source> getEventSource();
}
