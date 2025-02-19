package top.spray.engine.process.processor.factory;

import top.spray.core.dynamic.SprayClassLoader;
import top.spray.core.factory.SprayMaterial;
import top.spray.engine.process.processor.dispatch.coordinate.coordinator.SprayProcessCoordinator;
import top.spray.engine.process.processor.execute.step.meta.SprayProcessExecuteStepMeta;

public class SprayExecutorMaterial extends SprayMaterial {
    private final SprayProcessCoordinator coordinator;
    private final SprayProcessExecuteStepMeta stepMeta;
    private final SprayClassLoader classLoader;

    public SprayExecutorMaterial(SprayProcessCoordinator coordinator, SprayProcessExecuteStepMeta stepMeta, SprayClassLoader classLoader) {
        this.coordinator = coordinator;
        this.stepMeta = stepMeta;
        this.classLoader = classLoader;
    }

    public SprayProcessCoordinator getCoordinator() {
        return coordinator;
    }

    public SprayProcessExecuteStepMeta getStepMeta() {
        return stepMeta;
    }

    public SprayClassLoader getClassLoader() {
        return classLoader;
    }
}