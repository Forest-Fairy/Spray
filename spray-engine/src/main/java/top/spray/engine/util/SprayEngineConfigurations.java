package top.spray.engine.util;

import top.spray.core.config.model.SprayConfigObj;
import top.spray.core.config.model.SpraySystemConfiguration;

public class SprayEngineConfigurations {
    private static final SprayConfigObj<Integer> EXECUTOR_THREAD_CONCURRENCY_MAX =
            SpraySystemConfiguration.Builder(
                    SpraySystemConfiguration.CONFIG_PREFIX + "executor.thread.concurrency.max",
                    null, 50)
                    .build("executor.thread.concurrency.max", true);
    private static final SprayConfigObj<Integer> EXECUTOR_THREAD_CAPACITY_MAX =
            SpraySystemConfiguration.Builder(
                    SpraySystemConfiguration.CONFIG_PREFIX + "executor.thread.capacity.max",
                    null, 100)
                    .build("executor.thread.capacity.max", true);
    private static final SprayConfigObj<Boolean> EXECUTOR_REMOTING_SUPPORT =
            SpraySystemConfiguration.Builder(
                    SpraySystemConfiguration.CONFIG_PREFIX + "executor.remoting.support",
                    null, true)
                    .build("executor.remoting.support", true);

    public static int executorMaxThreadConcurrency() {
        return EXECUTOR_THREAD_CONCURRENCY_MAX.getValue();
    }

    public static int executorMaxThreadCapacity() {
        return EXECUTOR_THREAD_CAPACITY_MAX.getValue();
    }

    public static boolean executorRemotingSupport() {
        return EXECUTOR_REMOTING_SUPPORT.getValue();
    }


}
