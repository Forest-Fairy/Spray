package top.spray.engine.step.executor;

import top.spray.engine.base.handler.listen.SprayListener;
import top.spray.engine.step.method.SprayProcessStepMethod;

public interface SprayExecutorListener extends SprayListener {
    void handle(SprayProcessStepExecutor executor, SprayProcessStepMethod executeMethod, Object... methodArgs);
}
