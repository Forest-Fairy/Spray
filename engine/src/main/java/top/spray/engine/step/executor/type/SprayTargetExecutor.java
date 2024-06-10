package top.spray.engine.step.executor.type;

import top.spray.engine.step.executor.SprayProcessStepExecutor;

public interface SprayTargetExecutor extends SprayProcessStepExecutor {
    @Override
    default String executorType() {
        return "target";
    }
}