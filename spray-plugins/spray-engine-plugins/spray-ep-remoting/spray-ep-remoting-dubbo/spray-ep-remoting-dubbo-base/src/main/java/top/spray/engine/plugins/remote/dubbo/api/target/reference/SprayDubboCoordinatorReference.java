package top.spray.engine.plugins.remote.dubbo.api.target.reference;

public interface SprayDubboCoordinatorReference {
    int getCoordinatorStatus(String transactionId);

    int createExecutorCount(String transactionId);

    /** a method for executor to publish its data */
    void dispatch(String transactionId, String variablesIdentityDataKey, String fromExecutorNameKey, String data, boolean still);



}