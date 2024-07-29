package top.spray.engine.plugins.remote.dubbo.util;

import top.spray.core.config.model.SprayConfigObj;
import top.spray.core.config.model.SpraySystemConfiguration;

public class SprayDubboConfigurations {

    private static final SprayConfigObj<String> DUBBO_APPLICATION_NAME =
            SpraySystemConfiguration.Builder(
                            "dubbo.application.name",
                            null, "spray-remoting-dubbo")
                    .build("dubbo.application.name", true);

    private static final SprayConfigObj<Integer> DUBBO_SERVICE_PROVIDER_PORT =
            SpraySystemConfiguration.Builder(
                    "dubbo.protocol.port",
                    null, 20881)
                    .build("dubbo.protocol.port", true);
    private static final SprayConfigObj<Integer> DUBBO_SERVICE_BEAN_MAX_CACHE_TIME =
            SpraySystemConfiguration.Builder(
                    SpraySystemConfiguration.CONFIG_PREFIX + "dubbo.bean.cache.time.max",
                    null, 3600000)
                    .build("dubbo.bean.cache.time.max", true);
    private static final SprayConfigObj<Integer> DUBBO_EXECUTOR_DEFAULT_TIME_OUT =
            SpraySystemConfiguration.Builder(
                    SpraySystemConfiguration.CONFIG_PREFIX + "dubbo.executors.timeout",
                    null, 3600000)
                    .build("dubbo.executors.timeout", true);

    public static String dubboApplicationName() {
        return DUBBO_APPLICATION_NAME.getValue();
    }
    public static int dubboServiceProviderPort() {
        return DUBBO_SERVICE_PROVIDER_PORT.getValue();
    }
    public static int dubboServiceBeanMaxCacheTime() {
        return DUBBO_SERVICE_BEAN_MAX_CACHE_TIME.getValue();
    }
    public static int dubboExecutorDefaultTimeOut() {
        return DUBBO_EXECUTOR_DEFAULT_TIME_OUT.getValue();
    }

}
