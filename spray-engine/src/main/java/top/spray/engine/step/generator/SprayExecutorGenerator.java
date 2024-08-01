package top.spray.engine.step.generator;

import top.spray.core.dynamic.loader.SprayClassLoader;
import top.spray.core.util.SprayServiceUtil;
import top.spray.engine.coordinate.meta.SprayProcessCoordinatorMeta;
import top.spray.engine.step.executor.SprayExecutorDefinition;
import top.spray.engine.step.meta.SprayProcessStepMeta;

import java.util.Map;

/**
 * defined the executor generator
 */
public interface SprayExecutorGenerator {
    static SprayExecutorDefinition generate(SprayProcessCoordinatorMeta coordinatorMeta, SprayProcessStepMeta stepMeta, SprayClassLoader classLoader) {
        Map<String, SprayExecutorGenerator> generatorMap = SprayServiceUtil.loadServiceClassNameMapCache(SprayExecutorGenerator.class);
        for (SprayExecutorGenerator generator : generatorMap.values()) {
            if (generator.support(coordinatorMeta, stepMeta, classLoader)) {
                return generator.generateExecutor(coordinatorMeta, stepMeta, classLoader);
            }
        }
        throw new IllegalArgumentException("no support generator found");
    }
    default boolean support(SprayProcessCoordinatorMeta coordinatorMeta, SprayProcessStepMeta stepMeta, SprayClassLoader classLoader) {
        return this.getClass().getName().equalsIgnoreCase(stepMeta.executorFactoryClass());
    }
    SprayExecutorDefinition generateExecutor(SprayProcessCoordinatorMeta coordinatorMeta, SprayProcessStepMeta stepMeta, SprayClassLoader classLoader);
}
