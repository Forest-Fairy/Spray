package top.spray.engine.process.util;

import top.spray.core.config.model.SprayConfigObj;
import top.spray.core.config.model.SpraySystemConfiguration;

@SuppressWarnings("unchecked")
public class SprayEngineConfigurations {
    private static final SprayConfigObj<Integer> EXECUTOR_THREAD_CONCURRENCY_MAX =
            SpraySystemConfiguration.Builder(
                    SpraySystemConfiguration.CONFIG_PREFIX + "executor.thread.concurrency.max",
                    null, 50)
                    .i18n("executor.thread.concurrency.max").visible(true).build();

    private static final SprayConfigObj<Integer> EXECUTOR_THREAD_TASK_CAPACITY_MAX =
            SpraySystemConfiguration.Builder(
                    SpraySystemConfiguration.CONFIG_PREFIX + "executor.thread.capacity.max",
                    null, 100)
                    .i18n("executor.thread.capacity.max").visible(true).build();

    private static final SprayConfigObj<Boolean> EXECUTOR_REMOTING_SUPPORT =
            SpraySystemConfiguration.Builder(
                    SpraySystemConfiguration.CONFIG_PREFIX + "executor.remoting.support",
                    null, true)
                    .i18n("executor.remoting.support").visible(true).build();

    public static int executorMaxThreadConcurrency() {
        return EXECUTOR_THREAD_CONCURRENCY_MAX.getValue();
    }

    public static int executorMaxThreadTaskCapacity() {
        return EXECUTOR_THREAD_TASK_CAPACITY_MAX.getValue();
    }

    public static boolean executorRemotingSupport() {
        return EXECUTOR_REMOTING_SUPPORT.getValue();
    }


}
