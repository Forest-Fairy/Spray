package top.spray.engine.remote.dubbo.api;

public interface SprayDubboReference {
    /**
     * require the remote server to generate a dubbo service with meta json
     */
    boolean generateExecutor(String transactionId, String executorNameKey, String coordinatorMeta, String executorMeta);

    /** require the remote server to shut down the remote coordinator service */
    boolean whenCoordinatorShutDown(String transactionId);

}
