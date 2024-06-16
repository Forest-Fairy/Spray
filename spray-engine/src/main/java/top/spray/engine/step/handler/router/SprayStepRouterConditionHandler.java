package top.spray.engine.step.handler.router;
import top.spray.core.engine.props.SprayData;
import top.spray.engine.step.executor.SprayProcessStepExecutor;
import top.spray.engine.step.handler.SprayExecutorHandler;
import top.spray.engine.step.meta.SprayProcessStepMeta;

public interface SprayStepRouterConditionHandler extends SprayExecutorHandler {
    boolean match(SprayProcessStepMeta currentStepMeta, SprayProcessStepExecutor fromExecutor, SprayData data, boolean still);
}
