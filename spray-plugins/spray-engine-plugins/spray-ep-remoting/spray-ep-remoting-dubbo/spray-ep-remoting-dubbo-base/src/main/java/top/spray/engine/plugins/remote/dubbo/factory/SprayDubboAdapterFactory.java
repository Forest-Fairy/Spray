package top.spray.engine.plugins.remote.dubbo.factory;

import top.spray.core.engine.props.SprayData;
import top.spray.engine.factory.SprayExecutorFactory;
import top.spray.engine.plugins.remote.dubbo.api.target.SprayDubboCoordinator;
import top.spray.engine.step.executor.SprayProcessStepExecutor;
import top.spray.engine.step.meta.SprayProcessStepMeta;

public class SprayDubboAdapterFactory {

    public static boolean generateAdapter(String transactionId, String executorNameKey, String coordinatorMeta, String executorMeta) {
        SprayDubboBaseTargetService adapter = Adapters.get(transactionId);
        if (adapter == null) {
            synchronized (adapter) {
                if((adapter = Adapters.get(transactionId)) == null) {
                    Adapters.put(transactionId, );
                }
            }
        }
        return coordinator.registerExecutor(executorNameKey, createAdapter(coordinator, executorMeta));
    }


    public static SprayDubboBaseTargetService getAdapter(String transactionId, String executorNameKey, String coordinatorMeta, String executorMeta) {

    }

    private static SprayDubboBaseTargetService createAdapter(SprayDubboCoordinator coordinator, String executorMeta) {
        return new SprayDubboAdapterImpl(coordinator,
                );
    }

    public static void generateExecutor(SprayDubboCoordinator coordinator,
                                           String executorNameKey, String executorMeta) {
        SprayProcessStepMeta stepMeta = new SprayProcessStepMeta(SprayData.fromJson(executorMeta));
        SprayProcessStepExecutor executor = SprayExecutorFactory.create(
                coordinator, stepMeta, false);
        coordinator.registerExecutor(executorNameKey, executor);
    }
}
