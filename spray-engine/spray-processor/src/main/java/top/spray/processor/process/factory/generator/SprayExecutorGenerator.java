package top.spray.processor.process.factory.generator;

/**
 * defined the executor generator
 */
//public interface SprayExecutorGenerator {
//    static SprayStepExecutorFacade generate(SprayProcessCoordinatorMeta coordinatorMeta, SprayProcessStepMeta stepMeta, SprayClassLoader classLoader) {
//        Map<String, SprayExecutorGenerator> generatorMap = SprayServiceUtil.loadServiceClassNameMapCache(SprayExecutorGenerator.class);
//        for (SprayExecutorGenerator generator : generatorMap.values()) {
//            if (generator.support(coordinatorMeta, stepMeta, classLoader)) {
//                return generator.generateExecutor(coordinatorMeta, stepMeta, classLoader);
//            }
//        }
//        throw new IllegalArgumentException("no support generator found");
//    }
//    default boolean support(SprayProcessCoordinatorMeta coordinatorMeta, SprayProcessStepMeta stepMeta, SprayClassLoader classLoader) {
//        return this.getClass().getName().equalsIgnoreCase(stepMeta.executorFactoryClass());
//    }
//    SprayStepExecutorFacade generateExecutor(SprayProcessCoordinatorMeta coordinatorMeta, SprayProcessStepMeta stepMeta, SprayClassLoader classLoader);
//}
