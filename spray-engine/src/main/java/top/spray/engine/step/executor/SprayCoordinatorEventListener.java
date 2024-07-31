package top.spray.engine.step.executor;

import top.spray.core.engine.handler.listen.SprayListener;
import top.spray.engine.step.method.SprayProcessStepMethod;

public interface SprayCoordinatorEventListener extends SprayListener {
    void handle(SprayProcessStepExecutor executor, SprayProcessStepMethod executeMethod, Object... methodArgs);
}
