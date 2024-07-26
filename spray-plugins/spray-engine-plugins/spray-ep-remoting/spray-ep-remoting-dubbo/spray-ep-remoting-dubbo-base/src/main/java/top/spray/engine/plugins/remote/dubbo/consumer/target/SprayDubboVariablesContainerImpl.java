package top.spray.engine.plugins.remote.dubbo.consumer.target;

import top.spray.core.engine.props.SprayData;
import top.spray.engine.plugins.remote.dubbo.api.target.SprayDubboVariablesContainer;
import top.spray.engine.prop.SprayVariableContainer;
import top.spray.engine.plugins.remote.dubbo.api.target.reference.SprayDubboVariablesReference;
import top.spray.engine.step.executor.SprayProcessStepExecutor;

public class SprayDubboVariablesContainerImpl implements SprayDubboVariablesContainer {

    private final SprayDubboVariablesReference dubboVariablesReference;
    private final String creator;
    private final long createTime;
    private final String identityDataKey;

    public SprayDubboVariablesContainerImpl(
            SprayDubboVariablesReference dubboVariablesReference,
            String creator, long createTime, String identityDataKey) {
        this.dubboVariablesReference = dubboVariablesReference;
        this.creator = creator;
        this.createTime = createTime;
        this.identityDataKey = identityDataKey;
    }

    @Override
    public SprayDubboVariablesReference getVariablesReference() {
        return dubboVariablesReference;
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
        return identityDataKey;
    }

    @Override
    public String nextKey(SprayProcessStepExecutor lastExecutor, SprayProcessStepExecutor curExecutor) {
        throw new SprayDubboOperationNotSupportException();
    }

    @Override
    public SprayVariableContainer banKey(String key) {
        return null;
    }

    @Override
    public SprayVariableContainer freeKey(String key) {
        return null;
    }

    @Override
    public String getJsonString(String key) {
        return null;
    }

    @Override
    public String suGetJsonString(String key) {
        return null;
    }

    @Override
    public Object get(String key) {
        return null;
    }

    @Override
    public Object suGet(String key) {
        return null;
    }

    @Override
    public <T> T get(String key, Class<T> tClass) {
        return null;
    }

    @Override
    public <T> T suGet(String key, Class<T> tClass) {
        return null;
    }

    @Override
    public <T> Object getOrElse(String key, T def) {
        return null;
    }

    @Override
    public <T> Object suGetOrElse(String key, T def) {
        return null;
    }

    @Override
    public <T> T computeIfAbsent(String key, T value, boolean ignoreBanned, boolean setBannedIfAbsent) {
        return null;
    }

    @Override
    public SprayVariableContainer set(String key, Object value, boolean setBanned) {
        return null;
    }

    @Override
    public Object remove(String key, boolean setBanned) {
        return null;
    }

    @Override
    public SprayData copyInto(SprayData data) {
        return null;
    }
}
