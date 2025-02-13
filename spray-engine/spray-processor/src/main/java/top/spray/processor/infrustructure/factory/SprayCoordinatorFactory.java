package top.spray.processor.infrustructure.factory;

import top.spray.processor.process.dispatch.coordinate.coordinator.SprayDefaultProcessCoordinator;
import top.spray.processor.process.dispatch.coordinate.coordinator.SprayProcessCoordinator;
import top.spray.processor.process.factory.generator.SprayCoordinatorGenerator;
import top.spray.processor.process.dispatch.coordinate.meta.SprayProcessCoordinatorMeta;

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
