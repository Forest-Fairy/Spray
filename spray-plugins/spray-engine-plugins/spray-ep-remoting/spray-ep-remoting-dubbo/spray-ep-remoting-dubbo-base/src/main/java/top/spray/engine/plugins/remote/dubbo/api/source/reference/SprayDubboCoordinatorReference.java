package top.spray.engine.plugins.remote.dubbo.api.source.reference;

public interface SprayDubboCoordinatorReference {
    int getCoordinatorStatus(String transactionId);

    int createExecutorCount(String transactionId);

    /** a method for executor to publish its data */
    void dispatch(String transactionId, String filteredNodeKeys, String variablesIdentityDataKey, String fromExecutorNameKey, byte[] bytes, boolean still);

    boolean ensureProviderCreated(String transactionId, String executorNameKey);

}