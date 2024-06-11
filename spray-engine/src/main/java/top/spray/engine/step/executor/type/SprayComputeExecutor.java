package top.spray.engine.step.executor.type;

import top.spray.engine.step.executor.SprayProcessStepExecutor;

public interface SprayComputeExecutor extends SprayProcessStepExecutor {
    @Override
    default String executorType() {
        return "compute";
    }
}
