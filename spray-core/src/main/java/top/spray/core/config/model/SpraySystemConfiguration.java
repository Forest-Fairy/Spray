package top.spray.core.config.model;


import top.spray.common.tools.SprayTester;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.URL;
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
        public SprayConfigObj<V> build(String i18n, boolean visitable) {
            return new SprayConfigObj<>(i18n, visitable, key, val, defVal);
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
            .build("jdk.version", true);

    public static final SprayConfigObj<String> SPRAY_VERSION =
            Builder(
                    SpraySystemConfiguration.CONFIG_PREFIX + "version",
                    null,
                    "24.1.7")
                    .build("spray.version", true);

    public static final SprayConfigObj<String> PROJECT_DIR =
            Builder(
                    SpraySystemConfiguration.CONFIG_PREFIX + "projectDir",
                    null,
                    Optional.ofNullable(SpraySystemConfiguration.class.getClassLoader().getResource("/"))
                            .map(URL::getPath)
                            .orElse(Paths.get(System.getProperty("user.dir")).toString()))
                    .build("projectDir", true);

    public static final SprayConfigObj<String> MAC_ADDRESS;
    static {
        MAC_ADDRESS =
                Builder(
                        SpraySystemConfiguration.CONFIG_PREFIX + "mac",
                        SprayTester.supply(() ->
                                Arrays.toString(NetworkInterface.getByInetAddress(InetAddress.getLocalHost()).getHardwareAddress()),
                                null),
                        "UNKNOWN_MAC_ADDRESS")
                .build("mac", true);
    }

}
