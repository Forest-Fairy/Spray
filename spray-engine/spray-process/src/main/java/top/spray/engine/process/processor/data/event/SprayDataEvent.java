package top.spray.engine.process.processor.data.event;

import top.spray.engine.process.infrastructure.listen.SprayListenEvent;

public interface SprayDataEvent<T> extends SprayListenEvent<T> {
    String dataKey();
}
