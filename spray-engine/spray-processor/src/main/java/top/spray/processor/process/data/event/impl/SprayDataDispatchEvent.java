package top.spray.processor.process.data.event.impl;

import top.spray.common.tools.Sprays;
import top.spray.core.global.prop.SprayUnsupportedOperation;
import top.spray.common.tools.SprayOptional;
import top.spray.processor.process.data.event.SprayDataEvent;
import top.spray.processor.process.dispatch.coordinate.coordinator.SprayProcessCoordinator;
import top.spray.processor.process.dispatch.event.SprayDispatchEvent;
import top.spray.processor.process.execute.step.meta.SprayOptionalData;

public class SprayDataDispatchEvent implements SprayDataEvent<SprayOptionalData>, SprayDispatchEvent<SprayOptionalData> {
    private final String eventId;
    private final String transactionId;
    private final String from;
    private final SprayOptionalData optionalData;
    private final SprayDataDispatchResultType dispatchResultType;
    private final long createTime;

    public SprayDataDispatchEvent(SprayProcessCoordinator coordinator, String from,
                                  SprayOptionalData optionalData,
                                  SprayDataDispatchResultType dispatchResultType) {
        this.eventId = Sprays.UUID();
        this.transactionId = coordinator.transactionId();
        this.from = from;
        this.optionalData = optionalData.copyIfPresent();
        this.dispatchResultType = dispatchResultType;
        this.createTime = System.currentTimeMillis();
    }

    public String getTransactionId() {
        return transactionId;
    }

    public String getFrom() {
        return from;
    }

    public SprayDataDispatchResultType dispatchResultType() {
        return dispatchResultType;
    }

    @Override
    public String dataKey() {
        return this.optionalData.getDataKey();
    }

    @Override
    public String transactionId() {
        return transactionId;
    }

    @Override
    public String from() {
        return from;
    }

    @Override
    public String getEventId() {
        return eventId;
    }

    @Override
    public long getEventTime() {
        return this.createTime;
    }

    @Override
    public void setAttr(String key, String val) {
        SprayUnsupportedOperation.unsupported();
    }

    @Override
    public String getAttr(String key) {
        return SprayUnsupportedOperation.unsupported();
    }

    @Override
    public SprayOptional<SprayOptionalData> getEventSource() {
        return SprayOptional.of(optionalData);
    }
}
