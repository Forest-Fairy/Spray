package top.spray.engine.process.processor.dispatch.variables;

import top.spray.common.data.SprayData;
import top.spray.common.data.SprayDataUtil;
import top.spray.common.tools.tuple.SprayTuples;
import top.spray.engine.process.infrastructure.prop.SprayVariableContainer;
import top.spray.engine.process.processor.dispatch.coordinate.coordinator.SprayProcessCoordinator;
import top.spray.engine.process.processor.execute.step.executor.facade.SprayStepFacade;

import java.util.function.Supplier;

public class SprayCoordinatorVariableContainer implements SprayVariableContainer {
    private final String creator;
    private final long createTime;
    private final SprayData data;
    private final String identityKey;

    private SprayCoordinatorVariableContainer(String creator, long createTime, SprayData data, String identityKey) {
        this.creator = creator;
        this.createTime = createTime;
        this.data = data;
        this.identityKey = identityKey;
    }

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
        return this;
    }

    @Override
    public SprayVariableContainer free(String key) {
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
        return this.get(key, Object.class);
    }

    @Override
    public <T> T get(String key, Class<T> tClass) {
        return _get(key, tClass);
    }
    @Override
    public <T> T suGet(String key, Class<T> tClass) {
        return _get(key, tClass);
    }

    @Override
    public <T> Object getOrElse(String key, T def) {
        return _getOrElse(key, def, (Class<T>) def.getClass());
    }
    @Override
    public <T> Object suGetOrElse(String key, T def) {
        return _getOrElse(key, def, (Class<T>) def.getClass());
    }

    @Override
    public <T> T computeIfAbsent(String key, boolean ignoreBanned, Supplier<SprayTuples._2<T, Boolean>> valueAndSetBanned) {
        if (valueAndSetBanned == null) {
            throw new IllegalArgumentException("can not compute with null cause null value is not allowed to exist in spray data");
        }
        T t = (T) this._get(key);
        if (t == null) {
            SprayTuples._2<T, Boolean> _2 = valueAndSetBanned.get();
            this.set(key, t = _2.t0(), _2.t1());
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

    private <T> T _getOrElse(String key, T def, Class<T> tClass) {
        T t = this._get(key, tClass);
        if (t == null) {
            t = def;
        }
        return t;
    }
    private <T> T _get(String key, Class<T> tClass) {
        return SprayDataUtil.convertValue(this._get(key), tClass);
    }

    private Object _get(String key) {
        return this.data.get(key);
    }

    public static SprayCoordinatorVariableContainer create(SprayProcessCoordinator coordinator) {
        String creator = coordinator.getMeta().getName() + "[" + coordinator.transactionId() + "]";
        long createTime = System.currentTimeMillis();
        SprayData data = SprayData.deepCopy(coordinator.getMeta().getDefaultProcessData());
        String key = createTime + "#" + creator;
        return new SprayCoordinatorVariableContainer(creator, createTime, data, key);
    }
}
