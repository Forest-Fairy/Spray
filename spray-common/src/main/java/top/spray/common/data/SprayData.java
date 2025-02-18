package top.spray.common.data;

import org.apache.commons.lang3.StringUtils;

import java.io.Serial;
import java.io.Serializable;
import java.util.*;

public class SprayData implements Map<String, Object>, Serializable {
    public static final SprayData EMPTY = new SprayData().unmodifiable();

    @Serial
    private static final long serialVersionUID = 102262040630237L;

    private final Map<String, Object> inside;

    public SprayData() {
        inside = new HashMap<>();
    }

    public SprayData(Map map) {
        inside = new HashMap<>();
        map.forEach((k, v) -> {
            if (k != null) {
                this.put(String.valueOf(k), v);
            }
        });
    }
    public SprayData(String key0, Object value0, Object... keyValues) {
        inside = new HashMap<>();
        if (key0 == null) {
            throw new IllegalArgumentException("key can not be null");
        }
        this.put(key0, value0);
        if (keyValues != null && keyValues.length > 0) {
            if (! (keyValues.length % 2 == 0)) {
                throw new IllegalArgumentException("keyValues must be key-value pairs!");
            }
            for (int i = 0; i < keyValues.length; i += 2) {
                if (keyValues[i] == null) {
                    throw new IllegalArgumentException("key can not be null");
                }
                this.put(keyValues[i].toString(), keyValues[i + 1]);
            }
        }
    }

    /**
     * a get method witch requires the status is not null
     */
    public <T> T getNoneNull(final String key, final Class<T> clazz) {
        return this.getNoneNull(key, clazz, null);
    }
    public <T> T getNoneNull(final String key, final Class<T> clazz, final Throwable throwable) {
        T t = this.get(key, clazz);
        if (t != null) {
            return t;
        } else {
            throw throwable == null ?
                    new NullPointerException("the value on " + key + " is null") :
                    new RuntimeException(throwable);
        }
    }

    public <T> T get(final String key, final Class<T> clazz) {
        return SprayDataUtil.convertValue(inside.get(key), clazz);
    }

    public <T> T getOrElse(final String key, final T defaultValue) {
        Object value = inside.get(key);
        if (value == null) {
            return defaultValue;
        } else {
            Class<?> tClass = Objects.requireNonNullElse(defaultValue, value).getClass();
            return (T) SprayDataUtil.convertValue(value, tClass);
        }
    }

    public String getString(final String key) {
        return get(key, String.class);
    }

    public <T> List<T> getList(String key, Class<T> tClass, boolean replace) {
        List<T> list = this.getList(key, tClass);
        if (replace && list != null) {
            this.put(key, list);
        }
        return list;
    }
    public <T> List<T> getList(String key, Class<T> tClass) {
        Object ol = this.get(key);
        if (ol == null) {
            return null;
        }
        if (ol instanceof List) {
            return (List<T>) ol;
        }
        try {
            List<?> o = (List<?>) ol;
            boolean useNew = false;
            List<T> list = new ArrayList<>(o.size());
            for (Object each : o) {
                if (each == null) {
                    list.add(null);
                } else {
                    if (!useNew) {
                        useNew = true;
                    }
                    list.add(SprayDataUtil.convertValue(each, tClass));
                }
            }
            return useNew ? list : (List<T>) o;
        } catch (Exception e) {
            throw new RuntimeException("failed to cast object value to list value", e);
        }
    }

    public SprayData append(String key, Object val) {
        this.put(key, val);
        return this;
    }
    public SprayData keyBanned(String... keys) {
        return new KeysBannedData(this, Set.of(keys));
    }

    /* create unmodifiable */

    /**
     * get an unmodifiable data with cur data
     */
    public SprayData unmodifiable() {
        return new UnmodifiableData(new SprayDataWrapper(this));
    }
    public static SprayData Unmodifiable(Map<String, Object> data) {
        return new UnmodifiableData(new SprayDataWrapper(data));
    }
    public static SprayData NoneCopy(Map<String, Object> data) {
        return new SprayData(new SprayDataWrapper(data));
    }
    private record SprayDataWrapper(Map<String, Object> data) {
    }
    private SprayData(SprayDataWrapper dataWrapper) {
        inside = dataWrapper.data;
    }

    @SuppressWarnings("deprecation")
    public String toJson() {
        return toJson(false);
    }
    public String toJson(boolean pretty) {
        return SprayDataUtil.toJson(this, pretty);
    }

    public static SprayData fromJson(String json) {
        if (StringUtils.isBlank(json)) { return null; }
        json = json.trim();
        if (! json.startsWith("{") && ! json.endsWith("}")) {
            throw new RuntimeException("invalid json string");
        }
        return SprayDataUtil.parseToSprayData(json);
    }

    public static SprayData deepCopy(Map<?, ?> map) {
        if (map == null) {
            return new SprayData();
        }
        return SprayDataUtil.parseToSprayData(SprayDataUtil.toJson(map));
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
        return key != null && inside.get(key.toString()) != null;
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
    public Set<Entry<String, Object>> entrySet() {
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
        private UnmodifiableData(SprayDataWrapper dataWrapper) {
            super(dataWrapper);
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