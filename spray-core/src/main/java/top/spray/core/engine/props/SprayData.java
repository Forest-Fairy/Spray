package top.spray.core.engine.props;

import org.apache.commons.lang3.StringUtils;
import top.spray.core.util.JsonUtil;

import java.io.Serial;
import java.io.Serializable;
import java.util.*;

public class SprayData implements Map<String, Object>, Serializable {
    @Serial
    private static final long serialVersionUID = 102262040630237L;

    private final LinkedHashMap<String, Object> inside;

    public SprayData(Object... keyValues) {
        if (keyValues != null) {
            if (keyValues.length > 1 && keyValues.length % 2 == 0) {
                inside = new LinkedHashMap<>();
                for (int i = 0; i < keyValues.length; i += 2) {
                    if (keyValues[i] == null) {
                        throw new IllegalArgumentException("key can not be null");
                    }
                    inside.put(String.valueOf(keyValues[i]), keyValues[i + 1]);
                }
            } else if (keyValues.length == 1 && keyValues[0] instanceof Map map) {
                inside = new LinkedHashMap<>();
                map.forEach((k, v) -> {
                    if (k != null) {
                        inside.put(String.valueOf(k), v);
                    }
                });
            } else {
                throw new IllegalArgumentException("SprayData must be initialize with a map or key-value pairs!");
            }
        } else {
            inside = new LinkedHashMap<>();
        }
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
    public SprayData keyBanned(String... keys) {
        return new KeysBannedData(this, Set.of(keys));
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
        if (key == null) {
            throw new IllegalArgumentException("key can not be null");
        }
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

    static class KeysBannedData extends SprayData {
        private final Set<String> keysBanned;
        private KeysBannedData(final SprayData data, final Set<String> keysBanned) {
            super(data);
            this.keysBanned = keysBanned;
        }

        @Override
        public Object put(final String key, final Object value) {
            if (keysBanned.contains(key)) {
                throw new UnsupportedOperationException(key + " can not be operate in current data");
            }
            return super.put(key, value);
        }

        @Override
        public void putAll(final Map map) {
            HashSet<Object> tmpKeySet = new HashSet<Object>(map.keySet());
            for (Object o : tmpKeySet) {
                if (o != null && keysBanned.contains(String.valueOf(o))) {
                    throw new UnsupportedOperationException(o + " can not be operate in current data");
                }
            }
            map.forEach((k, v) -> {
                if (k != null) {
                    super.put(String.valueOf(k), v);
                }
            });
        }

        @Override
        public Object remove(final Object key) {
            if (keysBanned.contains(key)) {
                throw new UnsupportedOperationException(key + " can not be operate in current data");
            } else {
                return super.remove(key);
            }
        }
    }
}