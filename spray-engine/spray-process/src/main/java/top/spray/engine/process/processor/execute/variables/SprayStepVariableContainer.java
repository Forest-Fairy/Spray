package top.spray.engine.process.processor.execute.variables;

import top.spray.common.data.SprayData;
import top.spray.common.data.SprayDataUtil;
import top.spray.common.tools.tuple.SprayTuples;
import top.spray.engine.process.infrastructure.prop.SprayVariableContainer;
import top.spray.engine.process.processor.execute.step.executor.facade.SprayStepFacade;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;

public class SprayStepVariableContainer implements SprayVariableContainer {
    private final String creator;
    private final long createTime;
    private final SprayData data;
    private final String identityKey;
    private final SprayVariableContainer ancestor;
    private final Set<String> bannedKeys;

    private SprayStepVariableContainer(SprayVariableContainer ancestor, String creator, long createTime, SprayData data, String identityKey) {
        this.creator = creator;
        this.createTime = createTime;
        this.data = data;
        this.identityKey = identityKey;
        this.ancestor = ancestor;
        this.bannedKeys = new HashSet<>(0);
    }

//    public SprayVariableContainer ancestor() {
//        return ancestor;
//    }

    @Override
    public String creator() {
        return creator;
    }

    @Override
    public long createTime() {
        return createTime;
    }

    @Override
    public String identityDataKey() {
        return identityKey;
    }

    @Override
    public String nextKey(SprayStepFacade lastExecutor, SprayStepFacade curExecutor) {
        return SprayVariableContainer.generateKey(lastExecutor, this, curExecutor);
    }

    @Override
    public SprayVariableContainer ban(String key) {
        this.bannedKeys.add(key);
        return this;
    }

    @Override
    public SprayVariableContainer free(String key) {
        this.bannedKeys.remove(key);
        return this;
    }

    @Override
    public String getJsonString(String key) {
        Object o = this.get(key);
        if (o == null) {
            return null;
        }
        return SprayDataUtil.toJson(o);
    }

    @Override
    public String suGetJsonString(String key) {
        Object o = this.suGet(key);
        if (o == null) {
            return null;
        }
        return SprayDataUtil.toJson(o);
    }

    @Override
    public Object get(String key) {
        return this.get(key, Object.class);
    }
    @Override
    public Object suGet(String key) {
        return this.suGet(key, Object.class);
    }

    @Override
    public <T> T get(String key, Class<T> tClass) {
        return _get(false, key, tClass);
    }
    @Override
    public <T> T suGet(String key, Class<T> tClass) {
        return _get(true, key, tClass);
    }

    @Override
    public <T> Object getOrElse(String key, T def) {
        return _getOrElse(false, key, def, (Class<T>) def.getClass());
    }
    @Override
    public <T> Object suGetOrElse(String key, T def) {
        return _getOrElse(true, key, def, (Class<T>) def.getClass());
    }

    @Override
    public <T> T computeIfAbsent(String key, boolean ignoreBanned, Supplier<SprayTuples._2<T, Boolean>> valueAndSetBanned) {
        if (valueAndSetBanned == null) {
            throw new IllegalArgumentException("can not compute with null cause null value is not allowed to exist in spray data");
        }
        T t = (T) this._get(ignoreBanned, key);
        if (t == null) {
            SprayTuples._2<T, Boolean> _2 = valueAndSetBanned.get();
            this.set(key, t = _2.t1(), _2.t2());
        }
        return t;
    }

    /**
     * set
     * @param setBanned set key banned when get from ancestor
     * @return this
     */
    @Override
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
    @Override
    public Object remove(String key, boolean setBanned) {
        if (setBanned) {
            this.bannedKeys.add(key);
        }
        return data.remove(key);
    }

    @Override
    public SprayData copyInto(SprayData data) {
        if (data == null) {
            data = new SprayData();
        }
        data.putAll(this.data);
        return data;
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
            if (!bannedKeys.contains(key)) {
                return this.ancestor.get(key);
            } else if (ignoreBanned) {
                return this.ancestor.suGet(key);
            }
        }
        return null;
    }
    public static SprayStepVariableContainer easyCopy(SprayStepFacade lastExecutor, SprayVariableContainer last, SprayStepFacade executor) {
        return new SprayStepVariableContainer(last,
                executor.executorNameKey(),
                System.currentTimeMillis(),
                last.copyInto(null),
                SprayVariableContainer.generateKey(lastExecutor, last, executor));
    }
    public static SprayStepVariableContainer deepCopy(SprayStepFacade lastExecutor, SprayVariableContainer last, SprayStepFacade executor) {
        return new SprayStepVariableContainer(last,
                executor.executorNameKey(),
                System.currentTimeMillis(),
                SprayData.deepCopy(last.copyInto(null)),
                SprayVariableContainer.generateKey(lastExecutor, last, executor));
    }

}
