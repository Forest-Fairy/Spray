package top.spray.engine.process.processor.execute.step.executor.type;

import top.spray.engine.process.processor.dispatch.coordinate.coordinator.SprayProcessCoordinator;
import top.spray.engine.process.processor.execute.step.executor.SprayStepExecutor;
import top.spray.engine.process.processor.execute.step.executor.facade.SprayStepExecutorOwner;
import top.spray.engine.process.processor.execute.step.executor.facade.SprayStepFacade;
import top.spray.engine.process.processor.execute.step.meta.SprayExecutorType;

public abstract class SprayTargetExecutor extends SprayStepExecutor {

    public SprayTargetExecutor(SprayStepExecutorOwner executorOwner, String transactionId) {
        super(executorOwner, transactionId);
    }

    @Override
    public SprayExecutorType executorType() {
        return SprayExecutorType.TARGET;
    }

}
