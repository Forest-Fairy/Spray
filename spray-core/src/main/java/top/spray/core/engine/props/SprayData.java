package top.spray.core.engine.props;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.Serial;
import java.io.Serializable;
import java.util.*;

/**
 * TODO 支持文件存储
 */
public class SprayData implements Map<String, Object>, Serializable {
    @Serial
    private static final long serialVersionUID = 102262040630237L;
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

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
        return value == null ? defaultValue : (T) value;
    }

    public Integer getInteger(final String key) {
        Object o = get(key);
        return o == null ? null : Integer.valueOf(o.toString().trim());
    }

    public int getInteger(final String key, final int defaultValue) {
        return get(key, defaultValue);
    }


    public Long getLong(final String key) {
        Object o = get(key);
        return o == null ? null : Long.valueOf(o.toString().trim());
    }

    public Double getDouble(final String key) {
        Object o = get(key);
        return o == null ? null : Double.valueOf(o.toString().trim());
    }


    public String getString(final String key) {
        return (String) get(key);
    }

    public Boolean getBoolean(final String key) {
        Object o = get(key);
        return o == null ? null : Boolean.valueOf(o.toString().trim());
    }

    public SprayData append(String key, Object val) {
        this.put(key, val);
        return this;
    }
    @SuppressWarnings("deprecation")
    public String toJson() {
        try {
            return OBJECT_MAPPER.writeValueAsString(this.inside);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
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
        try {
            return OBJECT_MAPPER.writeValueAsString(this.inside);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public SprayData unmodifiable() {
        return new UnmodifiableData(this);
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