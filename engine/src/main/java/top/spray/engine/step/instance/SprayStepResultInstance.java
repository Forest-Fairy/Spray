package top.spray.engine.step.instance;

import top.spray.engine.step.executor.SprayProcessStepExecutor;
import top.spray.engine.base.instance.SprayStepInstanceStatus;

/**
 * the step result instance
 *  - create by an executor with the only instance
 */
public interface SprayStepResultInstance<Executor extends SprayProcessStepExecutor> {
    void init();
    String id();
    /** 事务id */
    String transactionId();

    void setStatus(SprayStepInstanceStatus status);
    SprayStepInstanceStatus getStatus();
}
