package top.spray.engine.coordinate.coordinator;

import top.spray.engine.base.execute.SprayMetaDrive;
import top.spray.engine.base.result.SprayCoordinateResult;
import top.spray.engine.coordinate.meta.SprayProcessCoordinatorMeta;

import java.util.concurrent.CompletableFuture;
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

}
