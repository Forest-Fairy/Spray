package top.spray.engine.plugins.remote.dubbo.consumer.target;

import top.spray.engine.plugins.remote.dubbo.api.target.SprayDubboVariablesContainer;
import top.spray.engine.plugins.remote.dubbo.api.source.reference.SprayDubboVariablesReference;

public class SprayDubboVariablesContainerImpl implements SprayDubboVariablesContainer {
    private final String transactionId;
    private final SprayDubboVariablesReference dubboVariablesReference;
    private final String creator;
    private final long createTime;
    private final String identityDataKey;

    public SprayDubboVariablesContainerImpl(
            SprayDubboVariablesReference dubboVariablesReference,
            String transactionId, String identityDataKey, String creator, long createTime) {
        this.transactionId = transactionId;
        this.dubboVariablesReference = dubboVariablesReference;
        this.identityDataKey = identityDataKey;
        this.creator = creator;
        this.createTime = createTime;
    }

    @Override
    public String creator() {
        return this.creator;
    }

    @Override
    public long createTime() {
        return this.createTime;
    }

    @Override
    public String identityDataKey() {
        return this.identityDataKey;
    }

    @Override
    public String transactionId() {
        return this.transactionId;
    }

    @Override
    public SprayDubboVariablesReference getVariablesReference() {
        return this.dubboVariablesReference;
    }
}
