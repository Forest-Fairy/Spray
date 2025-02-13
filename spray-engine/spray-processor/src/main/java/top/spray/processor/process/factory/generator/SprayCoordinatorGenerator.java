package top.spray.processor.process.factory.generator;

import top.spray.common.bean.SprayServiceUtil;
import top.spray.processor.process.dispatch.coordinate.coordinator.SprayProcessCoordinator;
import top.spray.processor.process.dispatch.coordinate.meta.SprayProcessCoordinatorMeta;
import top.spray.processor.exception.coordinate.SprayCoordinatorGenerateError;

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
