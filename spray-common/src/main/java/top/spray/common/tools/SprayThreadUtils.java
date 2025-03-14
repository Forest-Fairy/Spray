package top.spray.common.tools;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SprayThreadUtils {

    public static void setThreadName(String name) {
        Thread.currentThread().setName(name);
    }

    public static void setThreadContextClassLoader(ClassLoader contextClassLoader) {
        Thread.currentThread().setContextClassLoader(contextClassLoader);
    }

    public static String threadId() {
        return String.valueOf(Thread.currentThread().getId());
    }

    public static void sleep(long ms, TimeUnit unit) throws InterruptedException {
        Thread.sleep(TimeUnit.MILLISECONDS.convert(ms, unit));
    }

    public static ScheduledExecutorService sleepInLoop(long ms, TimeUnit unit, Runnable runnable) {
        ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.scheduleAtFixedRate(runnable,
                0, ms, unit);
        return scheduledExecutorService;
    }
}
