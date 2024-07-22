package top.spray.core.thread;

import cn.hutool.core.thread.BlockPolicy;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class SprayPoolExecutorBuilder {
    private final int corePoolSize;
    private final int maximumPoolSize;
    private final long keepAliveTime;
    private final TimeUnit unit;
    private final SprayThreadFactory threadFactory;
    private final int queueCapacity;

    public SprayPoolExecutorBuilder(
            int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
            SprayThreadFactory threadFactory, int queueCapacity) {
        this.corePoolSize = corePoolSize;
        this.maximumPoolSize = maximumPoolSize;
        this.keepAliveTime = keepAliveTime;
        this.unit = unit;
        this.threadFactory = threadFactory;
        this.queueCapacity = queueCapacity;
    }
    public SprayPoolExecutor build() {
        return new SprayPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, unit,
                new LinkedBlockingQueue<>(queueCapacity), threadFactory, new BlockPolicy());
    }

}
