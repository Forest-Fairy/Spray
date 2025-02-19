package top.spray.engine.process.processor.factory;

import top.spray.core.dynamic.SprayClassLoader;
import top.spray.core.factory.SprayFactory;
import top.spray.core.factory.SprayMaterial;
import top.spray.engine.process.processor.execute.step.executor.SprayStepExecutor;
import top.spray.engine.process.processor.execute.step.executor.facade.SprayStepFacade;
import top.spray.engine.process.processor.execute.step.meta.SprayProcessExecuteStepMeta;

import java.io.File;
import java.lang.reflect.InvocationTargetException;

public class SprayStepExecutorFactory implements SprayFactory<SprayStepExecutor> {

    @Override
    public boolean support(SprayMaterial SprayMaterial) {
        return SprayMaterial instanceof SprayExecutorMaterial;
    }

    @Override
    public SprayStepExecutor produce(SprayMaterial material) {
        if (material instanceof SprayExecutorMaterial executorMaterial) {
            SprayClassLoader classLoader = executorMaterial.getClassLoader();
            SprayProcessExecuteStepMeta stepMeta = executorMaterial.getStepMeta();
            classLoader.addClasses(this.toFiles(stepMeta.jarFiles()));
            try {
                return (SprayStepExecutor) classLoader.loadClass(stepMeta.executorClass())
                        .getConstructor(SprayStepFacade.class)
                        .newInstance(executorMaterial.getCoordinator());
            } catch (InstantiationException | ClassNotFoundException | NoSuchMethodException |
                     InvocationTargetException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
            executorMaterial.getCoordinator().executorCreate();
        }
        return null;
    }

    private File[] toFiles(String s) {
    }

}
