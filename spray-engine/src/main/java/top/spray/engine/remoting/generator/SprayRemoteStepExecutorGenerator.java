package top.spray.engine.remoting.generator;

import top.spray.core.util.SprayClassLoader;
import top.spray.core.util.SprayServiceUtil;
import top.spray.engine.coordinate.coordinator.SprayProcessCoordinator;
import top.spray.engine.coordinate.generator.SprayCoordinatorGenerator;
import top.spray.engine.coordinate.meta.SprayProcessCoordinatorMeta;
import top.spray.engine.exception.SprayCoordinatorGenerateError;
import top.spray.engine.remoting.SprayRemoteStepExecutor;
import top.spray.engine.step.meta.SprayProcessStepMeta;

import java.util.Map;

public interface SprayRemoteStepExecutorGenerator {
    static SprayRemoteStepExecutor generate(
            SprayProcessCoordinatorMeta coordinatorMeta, SprayProcessStepMeta executorMeta, SprayClassLoader sprayClassLoader) {
        Map<String, SprayRemoteStepExecutorGenerator> generatorMap = SprayServiceUtil.loadServiceClassNameMapCache(SprayRemoteStepExecutorGenerator.class);
        for (SprayRemoteStepExecutorGenerator generator : generatorMap.values()) {
            if (generator.support(coordinatorMeta, executorMeta, sprayClassLoader)) {
                return generator.generateExecutor(coordinatorMeta, executorMeta, sprayClassLoader);
            }
        }
        throw new IllegalArgumentException("no support generator found");
    }

    boolean support(
            SprayProcessCoordinatorMeta coordinatorMeta, SprayProcessStepMeta executorMeta, SprayClassLoader sprayClassLoader);
    SprayRemoteStepExecutor generateExecutor(
            SprayProcessCoordinatorMeta coordinatorMeta, SprayProcessStepMeta executorMeta, SprayClassLoader sprayClassLoader);
}