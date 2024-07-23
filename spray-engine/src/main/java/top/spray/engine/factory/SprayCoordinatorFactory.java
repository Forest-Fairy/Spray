package top.spray.engine.factory;

import top.spray.engine.coordinate.coordinator.SprayDefaultProcessCoordinator;
import top.spray.engine.coordinate.coordinator.SprayProcessCoordinator;
import top.spray.engine.coordinate.generator.SprayCoordinatorGenerator;
import top.spray.engine.coordinate.meta.SprayProcessCoordinatorMeta;

public class SprayCoordinatorFactory {
    private SprayCoordinatorFactory() {}

    public static SprayProcessCoordinator createCoordinator(SprayProcessCoordinatorMeta coordinatorMeta, boolean tryDefault) {
        String generatorClass = coordinatorMeta.getGeneratorClass();
        if (generatorClass == null || generatorClass.isBlank()) {
            return new SprayDefaultProcessCoordinator(coordinatorMeta);
        } else {
            return SprayCoordinatorGenerator.generate(coordinatorMeta);
        }
    }

}
