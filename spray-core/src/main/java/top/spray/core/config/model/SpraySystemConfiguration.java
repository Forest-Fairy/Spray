package top.spray.core.config.model;


import top.spray.common.data.SprayDataUtil;
import top.spray.common.tools.SprayTester;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

@SuppressWarnings("unchecked")
public class SpraySystemConfiguration {
    public static final String CONFIG_PREFIX = "spray.system.configuration.";
    public static <V> Builder<V> Builder(String key, V val, V defVal) {
        return new Builder<>(key, val, defVal);
    }
    public static class Builder<V> {
        private final String key;
        private final V val;
        private final V defVal;
        private String i18n;
        private boolean visible;
        private Builder(String key, V val, V defVal) {
            this.key = key;
            this.val = val;
            this.defVal = defVal;
        }
        public Builder i18n(String i18n) {
            this.i18n = i18n;
            return this;
        }
        public Builder visible(boolean visible) {
            this.visible = visible;
            return this;
        }
        public SprayConfigObj<V> build() {
            return new SprayConfigObj<>(
                    i18n != null ? i18n
                            : (key.startsWith(CONFIG_PREFIX) ? key.substring(CONFIG_PREFIX.length()) : key)
                    , visible, key, val, defVal);
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
                    .i18n("jdk.version").visible(true).build();

    public static final SprayConfigObj<String> SPRAY_VERSION =
            Builder(SpraySystemConfiguration.CONFIG_PREFIX + "version",
                    null,
                    "24.1.7")
                    .i18n("spray.version").visible(true).build();

    public static final SprayConfigObj<String> PROJECT_DIR =
            Builder(
                    SpraySystemConfiguration.CONFIG_PREFIX + "projectDir",
                    null,
                    Optional.ofNullable(SpraySystemConfiguration.class.getClassLoader().getResource("/"))
                            .map(URL::getPath)
                            .orElse(Paths.get(System.getProperty("user.dir")).toString()))
                    .i18n("project.dir").visible(true).build();

    public static final SprayConfigObj<String> MAC_ADDRESS;
    static {
        MAC_ADDRESS =
                Builder(
                        SpraySystemConfiguration.CONFIG_PREFIX + "mac",
                        SprayTester.supply(() ->
                                Arrays.toString(NetworkInterface.getByInetAddress(InetAddress.getLocalHost()).getHardwareAddress()),
                                null),
                        "UNKNOWN_MAC_ADDRESS")
                        .i18n("mac").visible(true).build();
    }


    public static void fromMap(Map<String, Object> map) {
        map.forEach((k, v) -> {
            SprayConfigObj<?> config = SprayConfigObj.CONFIG_MAP.get(k);
            if (config != null) {
                config.value = v == null ? null : SprayDataUtil.convertValue(v, config.type);
            } else {
                // register by new one
                new SprayConfigObj<>(k, false, k, v, v);
            }
        });
    }

}
