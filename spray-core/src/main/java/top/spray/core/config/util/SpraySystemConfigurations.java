package top.spray.core.config.util;

import top.spray.core.config.i18n.SpraySystemConfigurationName_i18n;
import top.spray.core.config.model.SprayConfigObj;
import top.spray.core.config.model.SpraySystemConfiguration;
import top.spray.core.i18n.Spray_i18n;
import top.spray.core.util.SprayDataUtil;

public class SpraySystemConfigurations {
    private SpraySystemConfigurations() {}
    public static String jdkVersion() {
        return SpraySystemConfiguration.JDK_VERSION.getValue();
    }
    public static String macAddress() {
        return SpraySystemConfiguration.MAC_ADDRESS.getValue();
    }
    public static String sprayVersion() {
        return SpraySystemConfiguration.SPRAY_VERSION.getValue();
    }
    public static String sprayProjectDir() {
        return SpraySystemConfiguration.SPRAY_PROJECT_DIR.getValue();
    }

    public static String getString(String key) {
        return getConfig(key, String.class);
    }
    public static <T> T getConfig(String key, Class<T> tClass) {
        SprayConfigObj<?> config = SpraySystemConfiguration.getConfig(key);
        if (tClass.isAssignableFrom(SprayConfigObj.class)) {
            return (T) config;
        }
        return config == null ? null : SprayDataUtil.convertValue(config.getValue(), tClass);
    }
}
