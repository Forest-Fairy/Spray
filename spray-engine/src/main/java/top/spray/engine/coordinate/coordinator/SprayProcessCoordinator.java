package top.spray.engine.coordinate.coordinator;

import top.spray.core.engine.execute.SprayMetaDrive;
import top.spray.core.engine.props.SprayData;
import top.spray.core.engine.result.SprayCoordinateStatus;
import top.spray.engine.coordinate.meta.SprayProcessCoordinatorMeta;
import top.spray.engine.step.executor.SprayProcessStepExecutor;
import top.spray.engine.step.meta.SprayProcessStepMeta;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.function.Supplier;

/**
 * Define the coordinator of a process
 */
public interface SprayProcessCoordinator extends SprayMetaDrive<SprayProcessCoordinatorMeta>, Supplier<SprayCoordinateStatus> {
    @Override
    SprayProcessCoordinatorMeta getMeta();

    @Override
    default SprayCoordinateStatus get() {
        return execute();
    }

    SprayCoordinateStatus execute();

    Executor getThreadExecutor();

    void registerExecutor(String executorId, SprayProcessStepExecutor executor);
    SprayProcessStepExecutor getThreadExecutor(String executorId);



    default void runNextNodes(SprayProcessStepExecutor fromExecutor, SprayData data, boolean still) {
        {
            // TODO check the last data run after all before sending data...

        }
        for (SprayProcessStepMeta nextNode : fromExecutor.getMeta().nextNodes()) {
            SprayProcessStepExecutor nextExecutor = SprayProcessStepExecutor.create(this, nextNode);
            if (nextExecutor.needWait(fromExecutor, data, still)) {
                // the executor need to wait for all data
                nextExecutor.dataInput(fromExecutor, data, still);
                continue;
            }
            if (nextNode.isAsync() && getThreadExecutor() != null) {
                getThreadExecutor().execute(nextExecutor.dataInput(fromExecutor, data, still));
            } else {
                nextExecutor.dataInput(fromExecutor, data, still).run();
            }
        }
    }

    void finish(SprayProcessStepExecutor sprayProcessStepExecutor);

    Map<String, Object> getProcessData();
}
