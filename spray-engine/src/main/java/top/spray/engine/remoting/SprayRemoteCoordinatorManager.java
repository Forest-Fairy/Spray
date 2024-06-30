package top.spray.engine.remoting;

import top.spray.engine.coordinate.meta.SprayProcessCoordinatorMeta;

public interface SprayRemoteCoordinatorManager {
    /** check if the remote coordinator has init */
    boolean hasInit(String transactionId);

    /** init and create a coordinator on remote server */
    void init(SprayProcessCoordinatorMeta coordinatorMeta);

    /** destroy the coordinator on the remote server */
    void destroy(String transactionId);








}