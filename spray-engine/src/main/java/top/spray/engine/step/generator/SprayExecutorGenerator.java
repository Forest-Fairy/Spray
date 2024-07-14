package top.spray.engine.step.generator;

import top.spray.core.util.SprayClassLoader;
import top.spray.engine.coordinate.coordinator.SprayProcessCoordinator;
import top.spray.engine.step.executor.SprayProcessStepExecutor;
import top.spray.engine.step.meta.SprayProcessStepMeta;

/**
 * defined the executor generator
 */
public interface SprayExecutorGenerator {
    static SprayProcessStepExecutor generateExecutor(String generatorClass, SprayProcessCoordinator coordinator, SprayProcessStepMeta stepMeta, SprayClassLoader classLoader) {

    }
    SprayProcessStepExecutor generateExecutor(SprayProcessCoordinator coordinator, SprayProcessStepMeta stepMeta, SprayClassLoader classLoader);
}
