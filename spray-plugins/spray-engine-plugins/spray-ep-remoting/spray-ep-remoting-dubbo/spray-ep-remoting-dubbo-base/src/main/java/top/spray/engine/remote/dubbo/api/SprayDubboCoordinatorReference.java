package top.spray.engine.remote.dubbo.api;

public interface SprayDubboCoordinatorReference {
    String getCoordinatorStatus(String transactionId);

    int createExecutorCount(String transactionId);

    /** a method for executor to publish its data */
    void dispatch(String transactionId, String variablesIdentityDataKey, String fromExecutorNameKey, String data, boolean still);



}