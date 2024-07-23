package top.spray.engine.coordinate.generator;

import top.spray.core.util.SprayServiceUtil;
import top.spray.engine.coordinate.coordinator.SprayProcessCoordinator;
import top.spray.engine.coordinate.meta.SprayProcessCoordinatorMeta;
import top.spray.engine.exception.SprayCoordinatorGenerateError;

import java.util.Map;

public interface SprayCoordinatorGenerator {

    static SprayProcessCoordinator generate(SprayProcessCoordinatorMeta coordinatorMeta, Object... args) {
        Map<String, SprayCoordinatorGenerator> generatorMap = SprayServiceUtil.loadServiceClassNameMapCache(SprayCoordinatorGenerator.class);
        try {
            for (SprayCoordinatorGenerator generator : generatorMap.values()) {
                if (generator.support(coordinatorMeta, args)) {
                    return generator.generateCoordinator(coordinatorMeta, args);
                }
            }
            throw new IllegalArgumentException("no support generator found");
        } catch (Exception e) {
            throw new SprayCoordinatorGenerateError(coordinatorMeta, e);
        }
    }

    boolean support(SprayProcessCoordinatorMeta coordinatorMeta, Object[] args);
    SprayProcessCoordinator generateCoordinator(SprayProcessCoordinatorMeta coordinatorMeta, Object[] args);
}
