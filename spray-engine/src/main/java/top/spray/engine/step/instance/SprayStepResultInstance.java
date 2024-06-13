package top.spray.engine.step.instance;

import top.spray.core.engine.props.SprayData;
import top.spray.engine.step.executor.SprayProcessStepExecutor;
import top.spray.core.engine.result.SprayStepStatus;

import java.util.Map;

/**
 * the step result instance
 *  - create by an executor with the only instance
 */
public interface SprayStepResultInstance<Executor extends SprayProcessStepExecutor> {
    void synchronizedInit();
    String id();
    default String transactionId() {
        return getExecutor().getCoordinator().getMeta().transactionId();
    }

    void setStatus(SprayStepStatus status);

    void setError(Throwable throwable);

    Executor getExecutor();

    void setInput(SprayData data);

    long getStartTime();
    long duration();

    Map<String, Long> inputInfos();

    Map<String, Long> outputInfos();
    SprayStepStatus getStatus();
}
