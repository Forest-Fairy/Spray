
package top.spray.engine.exception;

import top.spray.engine.coordinate.coordinator.SprayProcessCoordinator;
import top.spray.engine.step.meta.SprayProcessStepMeta;

public class SprayExecutorInitError extends RuntimeException {
    private SprayExecutorInitError(String msg, Throwable e) {
        super(msg, e);
    }

    public static SprayExecutorInitError errorWhenCreateExecutor(SprayProcessCoordinator coordinator, SprayProcessStepMeta stepMeta, Throwable e) {
        return new SprayExecutorInitError(String.format("error when create executor %s[%s] in process %s[%s]",
                        coordinator.getExecutorNameKey(stepMeta), stepMeta.executorClass(),
                        coordinator.getMeta().getName(), coordinator.getMeta().transactionId()),
                e);
    }
}
