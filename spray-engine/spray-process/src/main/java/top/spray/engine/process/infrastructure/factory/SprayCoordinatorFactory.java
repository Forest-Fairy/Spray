package top.spray.engine.process.infrastructure.factory;

import top.spray.engine.process.processor.dispatch.coordinate.coordinator.SprayDefaultProcessCoordinator;
import top.spray.engine.process.processor.dispatch.coordinate.coordinator.SprayProcessCoordinator;
import top.spray.engine.process.processor.factory.generator.SprayCoordinatorGenerator;
import top.spray.engine.process.processor.dispatch.coordinate.meta.SprayProcessCoordinatorMeta;

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
