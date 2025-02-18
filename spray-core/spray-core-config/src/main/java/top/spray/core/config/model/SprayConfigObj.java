package top.spray.core.config.model;

import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.TypeUtil;
import top.spray.common.data.SprayDataUtil;
import top.spray.core.i18n.SprayResourceBundle;
import top.spray.core.i18n.SprayResourceBundleDef;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

@SprayResourceBundle("system.configuration")
public class SprayConfigObj<T> {
    static final Map<String, SprayConfigObj<?>> CONFIG_MAP = new HashMap<>();
    private final String i18n;
    private final boolean visitable;
    private final String key;
    private T value;
    private final T defValue;

    private final Type type;

    SprayConfigObj(String i18n, boolean visitable, String key, T value, T defValue) {
        this.i18n = i18n;
        this.visitable = visitable;
        this.key = key;
        this.value = value;
        this.defValue = defValue;
        this.type = TypeUtil.getGenerics(this.getClass())[0].getActualTypeArguments()[0];
        CONFIG_MAP.put(key, this);
    }

    public boolean isVisitable() {
        return visitable;
    }

    public String getName() {
        return SprayResourceBundleDef.get("name", this.getClass(), i18n);
    }

    public String getComment() {
        return StrUtil.format(
                SprayResourceBundleDef.get("comment", this.getClass(), i18n),
                this.getValue(), this.defValue);
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

    public static void fromMap(Map<String, Object> map) {
        map.forEach((k, v) -> {
            SprayConfigObj<?> config = CONFIG_MAP.get(k);
            if (config != null) {
                config.value = v == null ? null : SprayDataUtil.convertValue(v, (Class<?>) config.type);
            } else {
                // register by new one
                new SprayConfigObj<>(k, false, k, v, null);
            }
        });
    }
}