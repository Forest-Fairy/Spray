package top.spray.engine.coordinate.remoting;

import top.spray.core.engine.props.SprayData;
import top.spray.core.thread.SprayPoolExecutor;
import top.spray.core.engine.status.impl.SprayCoordinateStatus;
import top.spray.engine.coordinate.meta.SprayProcessCoordinatorMeta;
import top.spray.engine.step.condition.SprayNextStepFilter;
import top.spray.engine.step.executor.SprayProcessStepExecutor;
import top.spray.engine.step.instance.SprayStepResultInstance;

/**
 * this coordinator will be created by remote
 */
public class SprayRemoteCoordinateAdapter implements SprayServerProcessCoordinator {
    public static SprayRemoteCoordinateAdapter create(SprayProcessCoordinatorMeta coordinatorMeta) {
        return new SprayRemoteCoordinateAdapter(coordinatorMeta);
    }

    private final SprayProcessCoordinatorMeta coordinatorMeta;

    private SprayRemoteCoordinateAdapter(SprayProcessCoordinatorMeta coordinatorMeta) {
        this.coordinatorMeta = coordinatorMeta;
    }

    @Override
    public SprayProcessCoordinatorMeta getMeta() {
        return this.coordinatorMeta;
    }




    @Override
    public void run() {

    }

    @Override
    public SprayCoordinateStatus status() {
        return null;
    }

    @Override
    public SprayPoolExecutor getSprayPoolExecutor() {
        return null;
    }

    @Override
    public void registerExecutor(String executorId, SprayProcessStepExecutor executor) {

    }

    @Override
    public SprayProcessStepExecutor getStepExecutor(String executorId) {
        return null;
    }

    @Override
    public int createExecutorCount() {
        return 0;
    }

    @Override
    public void dispatch(SprayProcessStepExecutor fromExecutor, SprayData data, boolean still, SprayNextStepFilter filter, boolean async) {

    }

    @Override
    public SprayStepResultInstance executeNext(SprayProcessStepExecutor nextStepExecutor, SprayProcessStepExecutor fromExecutor, SprayData data, boolean still) {
        return null;
    }

    @Override
    public void close() throws Exception {

    }
}
