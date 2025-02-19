package top.spray.core.config.model;

import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.TypeUtil;
import top.spray.common.data.SprayDataUtil;
import top.spray.core.i18n.SprayResourceBundle;
import top.spray.core.i18n.SprayResourceBundleDef;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

@SprayResourceBundle(SprayResourceBundle.CONFIGURATION)
public class SprayConfigObj<T> {
    protected static final Map<String, SprayConfigObj<?>> CONFIG_MAP = new HashMap<>();
    protected final String i18n;
    protected final boolean visitable;
    protected final String key;
    protected T value;
    protected final T defValue;
    protected final Class<?> type;

    SprayConfigObj(String i18n, boolean visitable, String key, T value, T defValue) {
        this.i18n = i18n;
        this.visitable = visitable;
        this.key = key;
        this.value = value;
        this.defValue = defValue;
        this.type = value == null ? defValue.getClass() : value.getClass();
        CONFIG_MAP.put(key, this);
    }

    public boolean isVisitable() {
        return visitable;
    }

    public String getName() {
        return SprayResourceBundleDef.get(
                "name", this.getClass(), i18n);
    }

    public String getComment() {
        return StrUtil.format(SprayResourceBundleDef.get(
                "comment", this.getClass(), i18n),
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

}