package top.spray.engine.remoting;

import org.apache.dubbo.config.ReferenceConfig;
import top.spray.engine.coordinate.meta.SprayProcessCoordinatorMeta;
import top.spray.engine.step.meta.SprayProcessStepMeta;

public class SprayRemoteCoordinatorManagerFactory {

    /**
     * This method is to create the coordinator manager on the remote server.
     */
    public static SprayRemoteCoordinatorManager createCoordinatorManager(SprayProcessCoordinatorMeta coordinatorMeta) {

    }

    /**
     * this method is to create a dubbo reference on local server.
     * it has better be recognized by its host and port.
     */
    public static SprayRemoteCoordinatorManager createExecutorCoordinatorManager(SprayProcessStepMeta stepMeta) {
        // TODO imitate from dubbo source code
        ReferenceConfig<SprayRemoteCoordinatorManager> referenceConfig = new ReferenceConfig<>();
        referenceConfig.setInterface(SprayRemoteCoordinatorManager.class);
        referenceConfig.setUrl("");


        return referenceConfig.get();
    }
}
