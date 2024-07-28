package top.spray.engine.plugins.remote.dubbo.api.target;

import top.spray.core.engine.props.SprayData;
import top.spray.core.util.SprayDataUtil;
import top.spray.engine.plugins.remote.dubbo.api.source.reference.SprayDubboVariablesReference;
import top.spray.engine.plugins.remote.dubbo.api.target.holder.SprayDubboVariablesReferenceHolder;
import top.spray.engine.prop.SprayVariableContainer;
import top.spray.engine.step.executor.SprayProcessStepExecutor;

/** this is a coordinator reference */
public interface SprayDubboVariablesContainer extends SprayDubboVariablesReferenceHolder, SprayVariableContainer {

    String transactionId();

    @Override
    default String nextKey(SprayProcessStepExecutor lastExecutor, SprayProcessStepExecutor curExecutor) {
        throw new Un;
    }

    @Override
    default SprayVariableContainer banKey(String key) {
        this.getVariablesReference().banKey(this.transactionId(), this.identityDataKey(), key);
        return this;
    }

    @Override
    default SprayVariableContainer freeKey(String key) {
        this.getVariablesReference().freeKey(this.transactionId(), this.identityDataKey(), key);
        return this;
    }

    @Override
    default String getJsonString(String key) {
        return this.getVariablesReference().getJsonString(this.transactionId(), this.identityDataKey(), key);
    }

    @Override
    default String suGetJsonString(String key) {
        return this.getVariablesReference().suGetJsonString(this.transactionId(), this.identityDataKey(), key);
    }

    @Override
    default Object get(String key) {
        return this.getVariablesReference().get(this.transactionId(), this.identityDataKey(), key);
    }

    @Override
    default Object suGet(String key) {
        return this.getVariablesReference().suGet(this.transactionId(), this.identityDataKey(), key);
    }

    @Override
    default <T> T get(String key, Class<T> tClass) {
        try {
            return SprayDataUtil.convertValue(this.getVariablesReference()
                            .get(this.transactionId(), this.identityDataKey(), key, tClass.getName()),
                    tClass);
        } catch (Exception e) {
            // TODO
            throw new x(e);
        }
    }

    @Override
    default <T> T suGet(String key, Class<T> tClass) {
        try {
            return SprayDataUtil.convertValue(this.getVariablesReference()
                            .suGet(this.transactionId(), this.identityDataKey(), key, tClass.getName()),
                    tClass);
        } catch (Exception e) {
            // TODO
            throw new x(e);
        }
    }

    @Override
    default <T> Object getOrElse(String key, T def) {
        return this.getVariablesReference()
                .getOrElse(this.transactionId(), this.identityDataKey(), key, def);
    }

    @Override
    default <T> Object suGetOrElse(String key, T def) {
        return this.getVariablesReference()
                .suGetOrElse(this.transactionId(), this.identityDataKey(), key, def);
    }

    @Override
    default <T> T computeIfAbsent(String key, T value, boolean ignoreBanned, boolean setBannedIfAbsent) {
        return this.getVariablesReference()
                .computeIfAbsent(this.transactionId(), this.identityDataKey(), key, value, ignoreBanned, setBannedIfAbsent);
    }

    @Override
    default SprayVariableContainer set(String key, Object value, boolean setBanned) {
        this.getVariablesReference()
                .set(this.transactionId(), this.identityDataKey(), key, value, setBanned);
        return this;
    }

    @Override
    default Object remove(String key, boolean setBanned) {
        return this.getVariablesReference()
                .remove(this.transactionId(), this.identityDataKey(), key, setBanned);
    }

    @Override
    default SprayData copyInto(SprayData data) {
        SprayData sprayData = this.getVariablesReference()
                .copyInto(this.transactionId(), this.identityDataKey(), data);
        if (data != null) {
            data.putAll(sprayData);
        } else {
            data = sprayData;
        }
        return data;
    }
}
