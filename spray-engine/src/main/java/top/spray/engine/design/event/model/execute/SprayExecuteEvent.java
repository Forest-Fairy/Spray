package top.spray.engine.design.event.model.execute;

import top.spray.engine.design.event.model.SprayBaseEvent;

public abstract class SprayExecuteEvent extends SprayBaseEvent {
    public final String transactionId;
    public final String currentExecutorNameKey;
    public SprayExecuteEvent(String transactionId, String currentExecutorNameKey, String eventName, long eventTime) {
        super(eventName, eventTime);
        this.transactionId = transactionId;
        this.currentExecutorNameKey = currentExecutorNameKey;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public String getCurrentExecutorNameKey() {
        return currentExecutorNameKey;
    }
}
