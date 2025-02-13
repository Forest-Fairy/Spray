package top.spray.processor.infrustructure.remoting.generator;

import top.spray.common.bean.SprayServiceUtil;
import top.spray.core.system.dynamic.SprayClassLoader;
import top.spray.processor.process.dispatch.coordinate.meta.SprayProcessCoordinatorMeta;
import top.spray.processor.process.execute.step.meta.SprayProcessExecuteStepMeta;
import top.spray.processor.infrustructure.remoting.SprayRemoteStepExecutorFacade;

import java.util.Map;

public interface SprayRemoteStepExecutorGenerator {
    static SprayRemoteStepExecutorFacade generate(
            SprayProcessCoordinatorMeta coordinatorMeta, SprayProcessExecuteStepMeta executorMeta, SprayClassLoader sprayClassLoader) {
        Map<String, SprayRemoteStepExecutorGenerator> generatorMap = SprayServiceUtil.loadServiceClassNameMapCache(SprayRemoteStepExecutorGenerator.class);
        for (SprayRemoteStepExecutorGenerator generator : generatorMap.values()) {
            if (generator.support(coordinatorMeta, executorMeta)) {
                return generator.generateExecutor(coordinatorMeta, executorMeta, sprayClassLoader);
            }
        }
        throw new IllegalArgumentException("no support generator found");
    }

    boolean support(
            SprayProcessCoordinatorMeta coordinatorMeta, SprayProcessExecuteStepMeta executorMeta);
    SprayRemoteStepExecutorFacade generateExecutor(
            SprayProcessCoordinatorMeta coordinatorMeta, SprayProcessExecuteStepMeta executorMeta, SprayClassLoader sprayClassLoader);
}
