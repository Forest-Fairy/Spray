package top.spray.processor.process.factory;

import top.spray.core.system.dynamic.SprayClassLoader;
import top.spray.core.system.factory.SprayMaterial;
import top.spray.processor.process.dispatch.coordinate.coordinator.SprayProcessCoordinator;
import top.spray.processor.process.execute.step.meta.SprayProcessExecuteStepMeta;

public class SprayExecutorMaterial implements SprayMaterial {
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