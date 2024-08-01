package top.spray.engine.remoting.generator;

import top.spray.core.dynamic.loader.SprayClassLoader;
import top.spray.core.util.SprayServiceUtil;
import top.spray.engine.coordinate.meta.SprayProcessCoordinatorMeta;
import top.spray.engine.remoting.SprayRemoteStepExecutorDefinition;
import top.spray.engine.step.meta.SprayProcessStepMeta;

import java.util.Map;

public interface SprayRemoteStepExecutorGenerator {
    static SprayRemoteStepExecutorDefinition generate(
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
    SprayRemoteStepExecutorDefinition generateExecutor(
            SprayProcessCoordinatorMeta coordinatorMeta, SprayProcessStepMeta executorMeta, SprayClassLoader sprayClassLoader);
}
