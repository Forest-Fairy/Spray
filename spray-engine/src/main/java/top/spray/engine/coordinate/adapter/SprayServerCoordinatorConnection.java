package top.spray.engine.coordinate.adapter;


import top.spray.core.engine.props.SprayData;
import top.spray.core.engine.result.SprayStepStatus;
import top.spray.engine.step.executor.SprayProcessStepExecutor;
import top.spray.engine.step.meta.SprayProcessStepMeta;

import java.io.Closeable;

/**
 * a connection from local to remote actual coordinator
 */
public interface SprayServerCoordinatorConnection extends Closeable {

    /**
     * send data to original coordinator
     */
    boolean sendData(SprayProcessStepExecutor executor, SprayData data, boolean isStill);

    SprayRemoteInstruction nextInstruction(SprayProcessStepMeta stepMeta, SprayStepStatus stepStatus);



    @Override
    void close();

}
