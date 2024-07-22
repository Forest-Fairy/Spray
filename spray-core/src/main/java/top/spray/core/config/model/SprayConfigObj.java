package top.spray.core.config.model;

import top.spray.core.config.i18n.SpraySystemConfigurationComment_i18n;
import top.spray.core.config.i18n.SpraySystemConfigurationName_i18n;
import top.spray.core.i18n.Spray_i18n;

import java.util.HashMap;
import java.util.Map;

public class SprayConfigObj<T> {
    static final Map<String, SprayConfigObj<?>> CONFIG_MAP = new HashMap<>();
    private final String i18n;
    private final String key;
    private final T value;
    private final T defValue;

    SprayConfigObj(String i18n, String key, T value, T defValue) {
        this.i18n = i18n;
        this.key = key;
        this.value = value;
        this.defValue = defValue;
        CONFIG_MAP.put(key, this);
    }

    public String getName() {
        return Spray_i18n.get(SpraySystemConfigurationName_i18n.class, i18n);
    }

    public String getComment() {
        return Spray_i18n.get(SpraySystemConfigurationComment_i18n.class, i18n);
    }

    public String getKey() {
        return key;
    }

    public T getValue() {
        if (value != null) {
            if (value instanceof String s) {
                if (s.isBlank()) {
                    return defValue;
                }
            }
            return value;
        }
        return defValue;
    }
}