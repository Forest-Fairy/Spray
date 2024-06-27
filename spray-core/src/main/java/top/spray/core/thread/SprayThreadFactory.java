package top.spray.core.thread;

import java.util.concurrent.ThreadFactory;

public interface SprayThreadFactory extends ThreadFactory {
    @Override
    SprayThread newThread(Runnable r);
}
