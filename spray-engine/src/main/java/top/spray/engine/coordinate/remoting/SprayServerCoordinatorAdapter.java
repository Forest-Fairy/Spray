package top.spray.engine.coordinate.remoting;


import top.spray.core.engine.props.SprayData;
import top.spray.core.engine.result.impl.SprayCoordinateStatus;
import top.spray.core.thread.SprayPoolExecutor;
import top.spray.engine.coordinate.coordinator.SprayProcessCoordinator;
import top.spray.engine.coordinate.meta.SprayProcessCoordinatorMeta;
import top.spray.engine.step.condition.SprayNextStepFilter;
import top.spray.engine.step.executor.SprayProcessStepExecutor;
import top.spray.engine.step.instance.SprayStepResultInstance;

import java.util.Map;

/**
 * a coordinator adapter for executor to contact with remote executor
 */
public interface SprayServerCoordinatorAdapter extends SprayProcessCoordinator {
    // TODO run coordinator methods by default.


    @Override
    SprayProcessCoordinatorMeta getMeta();

    @Override
    void run();

    @Override
    SprayCoordinateStatus status();

    @Override
    SprayPoolExecutor getSprayPoolExecutor();

    @Override
    void registerExecutor(String executorId, SprayProcessStepExecutor executor);

    @Override
    SprayProcessStepExecutor getStepExecutor(String executorId);

    @Override
    int createExecutorCount();

    @Override
    void dispatch(SprayProcessStepExecutor fromExecutor, SprayData data, boolean still, SprayNextStepFilter filter, boolean async);

    @Override
    SprayStepResultInstance executeNext(SprayProcessStepExecutor nextStepExecutor, SprayProcessStepExecutor fromExecutor, SprayData data, boolean still);

    @Override
    void close() throws Exception;
}
