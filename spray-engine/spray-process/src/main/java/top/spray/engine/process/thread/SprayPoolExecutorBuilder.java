package top.spray.engine.process.thread;

import cn.hutool.core.thread.BlockPolicy;
import top.spray.common.bean.SprayClassUtil;
import top.spray.core.thread.SprayPoolExecutor;
import top.spray.core.thread.SprayThreadFactory;
import top.spray.engine.process.processor.execute.step.meta.SprayProcessExecuteStepMeta;
import top.spray.engine.process.util.SprayEngineConfigurations;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.TimeUnit;

public class SprayPoolExecutorBuilder {
    private final ClassLoader classLoader;
    private int corePoolSize;
    private int maximumPoolSize;
    private long keepAliveTime;
    private TimeUnit unit;
    private SprayThreadFactory threadFactory;
    private int queueCapacity;
    private String blockHandlerClass;

    private SprayPoolExecutorBuilder(ClassLoader classLoader) {
        this.classLoader = classLoader;
        this.corePoolSize = 5;
        this.maximumPoolSize = 10;
        this.keepAliveTime = 30;
        this.unit = TimeUnit.SECONDS;
        this.threadFactory = SprayPoolExecutor.newDefaultFactory();
        this.queueCapacity = Integer.MAX_VALUE;
    }

    public SprayPoolExecutorBuilder buildWithMeta(SprayProcessExecuteStepMeta stepMeta) {
        this.corePoolSize     = stepMeta.coreThreadCount();
        this.maximumPoolSize  = stepMeta.maxThreadCount();
        this.keepAliveTime    = stepMeta.threadAliveTime();
        this.unit             = stepMeta.threadAliveTimeUnit();
        this.queueCapacity    = stepMeta.queueCapacity();
        this.blockHandlerClass          = stepMeta.blockHandlerClass();
        return this;
    }

    public SprayPoolExecutor build() {
        int core = Math.min(corePoolSize, SprayEngineConfigurations.executorMaxThreadConcurrency());
        int max = Math.min(maximumPoolSize, SprayEngineConfigurations.executorMaxThreadConcurrency());
        int cap = Math.min(queueCapacity, SprayEngineConfigurations.executorMaxThreadTaskCapacity());
        RejectedExecutionHandler handler = blockHandlerClass == null || blockHandlerClass.isEmpty() ? new BlockPolicy()
                : SprayClassUtil.newInstance((Class<? extends RejectedExecutionHandler>)
                        SprayClassUtil.getClassObject(classLoader, blockHandlerClass, false),
                new Class[0]);
        return new SprayPoolExecutor(
                core, Math.max(core, max), keepAliveTime, unit,
                new LinkedBlockingQueue<>(cap),
                threadFactory, handler);
    }

    public static SprayPoolExecutorBuilder newBuilder(ClassLoader classLoader) {
        return new SprayPoolExecutorBuilder(classLoader);
    }
}
