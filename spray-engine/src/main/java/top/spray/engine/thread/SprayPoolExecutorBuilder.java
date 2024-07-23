package top.spray.engine.thread;

import cn.hutool.core.thread.BlockPolicy;
import top.spray.core.thread.SprayPoolExecutor;
import top.spray.core.thread.SprayThreadFactory;
import top.spray.engine.util.SprayEngineConfigurations;

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
        int core = Math.min(corePoolSize, SprayEngineConfigurations.executorMaxThreadConcurrency());
        int max = Math.min(maximumPoolSize, SprayEngineConfigurations.executorMaxThreadConcurrency());
        int cap = Math.min(queueCapacity, SprayEngineConfigurations.executorMaxThreadCapacity());
        return new SprayPoolExecutor(
                core, Math.max(core, max), keepAliveTime, unit,
                new LinkedBlockingQueue<>(cap),
                threadFactory, new BlockPolicy());
    }

}
