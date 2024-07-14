package top.spray.engine.prop;

import cn.hutool.core.lang.generator.UUIDGenerator;
import top.spray.core.engine.props.SprayData;
import top.spray.core.util.SprayDataUtil;
import top.spray.engine.coordinate.coordinator.SprayProcessCoordinator;
import top.spray.engine.step.executor.SprayProcessStepExecutor;

import java.util.*;

public class SprayVariableContainer {
    private final String creator;
    private final long createTime;
    private final SprayData data;
    private final String key;
    private final SprayVariableContainer ancestor;
    private final Set<String> bannedKeys;

    private SprayVariableContainer(SprayVariableContainer ancestor, String creator, long createTime, SprayData data, String key) {
        this.creator = creator;
        this.createTime = createTime;
        this.data = data;
        this.key = key;
        this.ancestor = ancestor;
        this.bannedKeys = new HashSet<>(0);
    }
    public SprayVariableContainer ancestor() {
        return ancestor;
    }

    public String creator() {
        return creator;
    }

    public long createTime() {
        return createTime;
    }

    public String identityDataKey() {
        return key;
    }

    public String nextKey(SprayProcessStepExecutor lastExecutor, SprayProcessStepExecutor curExecutor) {
        return generateKey(lastExecutor, this, curExecutor);
    }

    public SprayVariableContainer banKey(String key) {
        this.bannedKeys.add(key);
        return this;
    }
    public SprayVariableContainer freeKey(String key) {
        this.bannedKeys.remove(key);
        return this;
    }

    public String getJsonString(String key) {
        Object o = this.get(key);
        if (o == null) {
            return null;
        }
        return SprayDataUtil.toJson(o);
    }
    public Object get(String key) {
        return this.get(key, Object.class);
    }
    public <T> T get(String key, Class<T> tClass) {
        return _get(false, key, tClass);
    }
    public <T> Object getOrElse(String key, T def) {
        return _getOrElse(false, key, def, (Class<T>) def.getClass());
    }

    public <T> T computeIfAbsent(String key, T value, boolean ignoreBanned, boolean setBannedIfAbsent) {
        if (value == null) {
            throw new IllegalArgumentException("can not compute with null cause null value is not allowed to exist in spray data");
        }
        T t = (T) this._get(ignoreBanned, key, value.getClass());
        if (t == null) {
            this.set(key, t = value, setBannedIfAbsent);
        }
        return t;
    }

    /**
     * set
     * @param setBanned set key banned when get from ancestor
     * @return this
     */
    public SprayVariableContainer set(String key, Object value, boolean setBanned) {
        if (setBanned) {
            this.bannedKeys.add(key);
        }
        data.put(key, value);
        return this;
    }

    /**
     * remove
     * @param setBanned set key banned when get from ancestor
     * @return existed value
     */
    public Object remove(String key, boolean setBanned) {
        if (setBanned) {
            this.bannedKeys.add(key);
        }
        return data.remove(key);
    }

    private <T> T _getOrElse(boolean ignoreBanned, String key, T def, Class<T> tClass) {
        T t = this._get(ignoreBanned, key, tClass);
        if (t == null) {
            t = def;
        }
        return t;
    }
    private <T> T _get(boolean ignoreBanned, String key, Class<T> tClass) {
        Object o = this._get(ignoreBanned, key);
        return SprayDataUtil.convertValue(o, tClass);
    }

    private Object _get(boolean ignoreBanned, String key) {
        Object o = this.data.get(key);
        if (o == null) {
            // try get from ancestors
            o = this._getFromAncestor(ignoreBanned, key);
        }
        return o;
    }
    private Object _getFromAncestor(boolean ignoreBanned, String key) {
        if (this.ancestor != null) {
            if (ignoreBanned || !bannedKeys.contains(key)) {
                return this.ancestor._get(ignoreBanned, key);
            }
        }
        return null;
    }

    public static SprayVariableContainer create(SprayProcessCoordinator coordinator) {
        String creator = coordinator.getMeta().getName() + "[" + coordinator.getMeta().transactionId() + "]";
        long createTime = System.currentTimeMillis();
        SprayData data = SprayData.deepCopy(coordinator.getMeta().getDefaultProcessData());
        String key = createTime + "#" + creator;
        return new SprayVariableContainer(null, creator, createTime, data, key);
    }
    public static SprayVariableContainer easyCopy(SprayProcessStepExecutor lastExecutor, SprayVariableContainer last, SprayProcessStepExecutor executor) {
        return new SprayVariableContainer(last,
                executor.getExecutorNameKey(),
                System.currentTimeMillis(),
                new SprayData(last.data),
                generateKey(lastExecutor, last, executor));
    }
    public static SprayVariableContainer deepCopy(SprayProcessStepExecutor lastExecutor, SprayVariableContainer last, SprayProcessStepExecutor executor) {
        return new SprayVariableContainer(last,
                executor.getExecutorNameKey(),
                System.currentTimeMillis(),
                SprayData.deepCopy(last.data),
                generateKey(lastExecutor, last, executor));
    }

    private static final String SEPARATOR = "_";
    private static String generateKey(SprayProcessStepExecutor fromExecutor, SprayVariableContainer last, SprayProcessStepExecutor executor) {
        SprayProcessCoordinator coordinator = executor.getCoordinator();
        if (fromExecutor == null) {
            // the first execute of the process, last is the process default variables
            return String.format("%s"+SEPARATOR+"%s(%s)", last.identityDataKey(),
                    executor.getMeta().getName(), executor.getExecutorNameKey());
        }
        return String.format("%s"+SEPARATOR+"%s(%s->%s)", last.identityDataKey(),
                executor.getMeta().getName(),
                fromExecutor.getExecutorNameKey(), executor.getExecutorNameKey());
    }
}
