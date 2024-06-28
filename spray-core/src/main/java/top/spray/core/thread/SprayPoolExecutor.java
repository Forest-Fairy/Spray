package top.spray.core.thread;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class SprayPoolExecutor extends ThreadPoolExecutor {
    public SprayPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, new SprayDefaultThreadFactory());
    }

    public SprayPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, new SprayDefaultThreadFactory(), handler);
    }

    public SprayPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, SprayThreadFactory threadFactory) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory);
    }

    public SprayPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, SprayThreadFactory threadFactory, RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);

    }

    public static SprayPoolExecutor newThreadPerTaskExecutor() {
        return new SprayPoolExecutor(0, Integer.MAX_VALUE, 0,
                TimeUnit.SECONDS, new LinkedBlockingQueue<>(),
                new SprayDefaultThreadFactory());
    }
    private static class SprayDefaultThreadFactory implements SprayThreadFactory {
        private static final AtomicInteger poolNumber = new AtomicInteger(1);
        private final ThreadGroup group;
        private final AtomicLong threadNumber = new AtomicLong(1);
        private final String namePrefix;

        SprayDefaultThreadFactory() {
            group = Thread.currentThread().getThreadGroup();
            namePrefix = "Spray-thread-pool-" + poolNumber.getAndIncrement() + "-thread-";
        }

        @Override
        public SprayThread newThread(Runnable r) {
            SprayThread t = new SprayThread(group, r,
                    namePrefix + threadNumber.getAndIncrement(),
                    0);
            if (t.isDaemon())
                t.setDaemon(false);
            if (t.getPriority() != Thread.NORM_PRIORITY)
                t.setPriority(Thread.NORM_PRIORITY);
            return t;
        }

    }
}
