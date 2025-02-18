package top.spray.core.factory;

import java.util.HashMap;
import java.util.Map;

public class SprayMaterial {

    private final Map<String, Object> props;

    public SprayMaterial(Map<String, Object> props) {
        this.props = new HashMap<>(props);
    }
    public void setProp(String key, Object val) {
        props.put(key, val);
    }
    public <T> T getProp(String key) {
        // noinspection unchecked
        return (T) props.get(key);
    }
}
