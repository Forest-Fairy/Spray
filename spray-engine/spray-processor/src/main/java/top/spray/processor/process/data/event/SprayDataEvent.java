package top.spray.processor.process.data.event;

import top.spray.processor.infrustructure.listen.SprayListenEvent;

public interface SprayDataEvent<T> extends SprayListenEvent<T> {
    String dataKey();
}
