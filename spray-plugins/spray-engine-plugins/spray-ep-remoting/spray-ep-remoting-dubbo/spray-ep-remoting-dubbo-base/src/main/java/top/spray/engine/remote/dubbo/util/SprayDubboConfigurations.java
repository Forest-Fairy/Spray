package top.spray.engine.remote.dubbo.util;

import top.spray.core.config.model.SprayConfigObj;
import top.spray.core.config.model.SpraySystemConfiguration;

public class SprayDubboConfigurations {
    private static final SprayConfigObj<Integer> DUBBO_SERVICE_BEAN_MAX_CACHE_TIME =
            SpraySystemConfiguration.Builder(
                    SpraySystemConfiguration.CONFIG_PREFIX + "dubbo.bean.cache.time.max",
                    null, 3600000)
                    .build("dubbo.bean.cache.time.max", true);

    public static int dubboServiceBeanMaxCacheTime() {
        return DUBBO_SERVICE_BEAN_MAX_CACHE_TIME.getValue();
    }

}
