package top.spray.engine.step.instance;

import top.spray.engine.step.executor.SprayProcessStepExecutor;
import top.spray.core.engine.result.SprayStepResult;

import java.util.Map;

/**
 * the step result instance
 *  - create by an executor with the only instance
 */
public interface SprayStepResultInstance<Executor extends SprayProcessStepExecutor> {
    void synchronizedInit();
    String id();
    String transactionId();

    Executor getExecutor();

    long getStartTime();
    long duration();

    Map<String, Long> inputInfos();

    Map<String, Long> outputInfos();

    void setStatus(SprayStepResult status);
    SprayStepResult getStatus();
}
