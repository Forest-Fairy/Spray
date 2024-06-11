package top.spray.engine.coordinate.coordinator;

import top.spray.engine.base.execute.SprayMetaDrive;
import top.spray.engine.base.props.SprayData;
import top.spray.engine.base.result.SprayCoordinateResult;
import top.spray.engine.coordinate.meta.SprayProcessCoordinatorMeta;
import top.spray.engine.step.executor.SprayProcessStepExecutor;
import top.spray.engine.step.meta.SprayProcessStepMeta;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Supplier;

/**
 * Define the coordinator of a process
 */
public interface SprayProcessCoordinator extends SprayMetaDrive<SprayProcessCoordinatorMeta>, Supplier<SprayCoordinateResult> {
    @Override
    SprayProcessCoordinatorMeta getMeta();

    @Override
    default SprayCoordinateResult get() {
        return execute();
    }

    SprayCoordinateResult execute();

    Executor getExecutor();

    void registerExecutor(String executorId, SprayProcessStepExecutor executor);
    SprayProcessStepExecutor getExecutor(String executorId);

    default void goToNextNodes(SprayProcessStepExecutor fromExecutor, SprayData data, boolean still) {
        for (SprayProcessStepMeta nextNode : fromExecutor.getMeta().nextNodes()) {
            SprayProcessStepExecutor stepExecutor = SprayProcessStepExecutor.create(this, nextNode);
            if (nextNode.isAsync()) {
                getExecutor().execute(stepExecutor.dataInput(fromExecutor, data, still));
            } else {
                stepExecutor.dataInput(fromExecutor, data, still).run();
            }
        }
    }
}
