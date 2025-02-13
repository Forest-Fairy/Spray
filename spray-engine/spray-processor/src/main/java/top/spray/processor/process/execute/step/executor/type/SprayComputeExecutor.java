package top.spray.processor.process.execute.step.executor.type;

import top.spray.processor.process.dispatch.coordinate.coordinator.SprayProcessCoordinator;
import top.spray.processor.process.execute.step.executor.SprayStepExecutor;
import top.spray.processor.process.execute.step.executor.facade.SprayStepFacade;
import top.spray.processor.process.execute.step.meta.SprayExecutorType;

public abstract class SprayComputeExecutor extends SprayStepExecutor {

    public SprayComputeExecutor(SprayProcessCoordinator coordinator, SprayStepFacade executorFacade) {
        super(coordinator, executorFacade);
    }

    @Override
    public SprayExecutorType executorType() {
        return SprayExecutorType.COMPUTE;
    }

}
