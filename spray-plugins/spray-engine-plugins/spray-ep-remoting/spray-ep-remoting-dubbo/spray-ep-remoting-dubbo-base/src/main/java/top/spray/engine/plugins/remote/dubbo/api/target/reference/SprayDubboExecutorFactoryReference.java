package top.spray.engine.plugins.remote.dubbo.api.target.reference;

/**
 * with this interface to call the remote server to create the dubbo service implement
 */
public interface SprayDubboExecutorFactoryReference {
    /**
     * require the remote server to generate a dubbo service with meta json
     */
    boolean generateExecutor(String transactionId, String executorNameKey, String coordinatorMeta, String executorMeta);

    /** require the remote server to shut down the remote coordinator service */
    boolean whenCoordinatorShutDown(String transactionId);

}
