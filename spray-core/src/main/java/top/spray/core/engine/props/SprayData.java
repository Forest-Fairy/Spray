package top.spray.core.engine.props;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONWriter;
import org.apache.commons.lang3.StringUtils;
import top.spray.core.util.JsonUtil;

import java.io.Serial;
import java.io.Serializable;
import java.util.*;

public class SprayData implements Map<String, Object>, Serializable {
    @Serial
    private static final long serialVersionUID = 102262040630237L;

    private final LinkedHashMap<String, Object> inside;

    public SprayData() {
        inside = new LinkedHashMap<>();
    }

    public SprayData(final String key, final Object value) {
        inside = new LinkedHashMap<>();
        inside.put(key, value);
    }

    public SprayData(final Map<String, Object> map) {
        inside = new LinkedHashMap<String, Object>(map);
    }


    public <T> T get(final String key, final Class<T> clazz) {
        return clazz.cast(inside.get(key));
    }

    public <T> T get(final String key, final T defaultValue) {
        Object value = inside.get(key);
        if (value == null) {
            return defaultValue;
        } else {
            if (defaultValue == null || defaultValue instanceof String ||
                    // class of the value can be assigned as the defaultValue
                    defaultValue.getClass().isAssignableFrom(value.getClass())) {
                return (T) value;
            }
            try {
                // TODO cast with value castor util
                return (T) defaultValue.getClass()
                        .getMethod("valueOf", String.class)
                        .invoke(null, value.toString().trim());
            } catch (Exception ignored) {
                return (T) value;
            }
        }
    }

    public String getString(final String key) {
        return (String) get(key);
    }

    public SprayData append(String key, Object val) {
        this.put(key, val);
        return this;
    }
    public SprayData unmodifiable() {
        return new UnmodifiableData(this);
    }

    public <T> List<T> getList(String key, Class<T> tClass) {
        List<?> o = (List<?>) this.get(key);
        if (o == null) {
            return null;
        }
        try {
            boolean useNew = false;
            List<T> list = new ArrayList<>(o.size());
            for (Object each : o) {
                if (each == null) {
                    list.add(null);
                } else if (each.getClass() == tClass) {
                    list.add((T) each);
                } else {
                    if (!useNew) {
                        useNew = true;
                    }
                    list.add(null);
                }
            }
            return useNew ? list : (List<T>) o;
        } catch (Exception e) {
            throw new RuntimeException("failed to cast object value to list value", e);
        }
    }
    @SuppressWarnings("deprecation")
    public String toJson() {
        return toJson(false);
    }
    public String toJson(boolean pretty) {
        return JsonUtil.toJson(this, pretty);
    }

    public static SprayData fromJson(String json) {
        if (StringUtils.isBlank(json)) { return null; }
        json = json.trim();
        if (! json.startsWith("{") && ! json.endsWith("}")) {
            throw new RuntimeException("invalid json string");
        }
        return JsonUtil.parseToSprayData(json);
    }

    public static SprayData deepCopy(Map<?, ?> map) {
        return JsonUtil.parseToSprayData(JsonUtil.toJson(map));
    }


    // Vanilla Map methods delegate to map field
    @Override
    public int size() {
        return inside.size();
    }

    @Override
    public boolean isEmpty() {
        return inside.isEmpty();
    }

    @Override
    public boolean containsValue(final Object value) {
        return inside.containsValue(value);
    }

    @Override
    public boolean containsKey(final Object key) {
        return inside.containsKey(key);
    }

    @Override
    public Object get(final Object key) {
        return inside.get(key);
    }

    @Override
    public Object put(final String key, final Object value) {
        return inside.put(key, value);
    }

    @Override
    public Object remove(final Object key) {
        return inside.remove(key);
    }

    @Override
    public void putAll(final Map<? extends String, ?> map) {
        inside.putAll(map);
    }

    @Override
    public void clear() {
        inside.clear();
    }

    @Override
    public Set<String> keySet() {
        return inside.keySet();
    }

    @Override
    public Collection<Object> values() {
        return inside.values();
    }

    @Override
    public Set<Map.Entry<String, Object>> entrySet() {
        return inside.entrySet();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        SprayData sprayData = (SprayData) o;

        if (!inside.equals(sprayData.inside)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return inside.hashCode();
    }

    @Override
    public String toString() {
        return this.toJson();
    }

    static class UnmodifiableData extends SprayData {
        private UnmodifiableData(final SprayData data) {
            super(data);
        }

        @Override
        public Object put(final String key, final Object value) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void putAll(final Map<? extends String, ?> map) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Object remove(final Object key) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void clear() {
            throw new UnsupportedOperationException();
        }
    }
}