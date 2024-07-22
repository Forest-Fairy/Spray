package top.spray.core.config.model;


import top.spray.core.config.i18n.SpraySystemConfigurationComment_i18n;
import top.spray.core.config.i18n.SpraySystemConfigurationName_i18n;
import top.spray.core.i18n.Spray_i18n;
import top.spray.core.util.SprayDataUtil;

import java.net.*;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Optional;

public class SpraySystemConfiguration {
    public static final String CONFIG_PREFIX = "spray.system.configuration.";
    public static <V> Builder<V> Builder(String key, V val, V defVal) {
        return new Builder<>(key, val, defVal);
    }
    public static class Builder<V> {
        private final String key;
        private final V val;
        private final V defVal;
        private Builder(String key, V val, V defVal) {
            this.key = key;
            this.val = val;
            this.defVal = defVal;
        }
        public SprayConfigObj<V> build(String i18n) {
            return new SprayConfigObj<>(i18n, key, val, defVal);
        }
    }

    public static SprayConfigObj<?> getConfig(String key) {
        return SprayConfigObj.CONFIG_MAP.get(key);
    }


    public static final SprayConfigObj<String> JDK_VERSION =
            Builder(
                    SpraySystemConfiguration.CONFIG_PREFIX + "jdk.version",
                    System.getProperty("java.specification.version"),
                    null)
            .build("jdk.version");

    public static final SprayConfigObj<String> SPRAY_VERSION =
            Builder(
                    SpraySystemConfiguration.CONFIG_PREFIX + "version",
                    null,
                    "24.1.7")
                    .build("spray.version");

    public static final SprayConfigObj<String> SPRAY_PROJECT_DIR =
            Builder(
                    SpraySystemConfiguration.CONFIG_PREFIX + "projectDir",
                    null,
                    Optional.ofNullable(SpraySystemConfiguration.class.getClassLoader().getResource("/"))
                            .map(URL::getPath)
                            .orElse(Paths.get(System.getProperty("user.dir")).toString()))
                    .build("spray.version");
    public static final SprayConfigObj<String> MAC_ADDRESS;
    static {
        String macAddress;
        try {
            macAddress = Arrays.toString(NetworkInterface.getByInetAddress(InetAddress.getLocalHost()).getHardwareAddress());
        } catch (SocketException | UnknownHostException ignored) {
            macAddress = null;
        }
        MAC_ADDRESS =
                Builder(
                        SpraySystemConfiguration.CONFIG_PREFIX + "mac",
                        macAddress,
                        "UNKNOWN_MAC_ADDRESS")
                .build("mac");
    }

}
