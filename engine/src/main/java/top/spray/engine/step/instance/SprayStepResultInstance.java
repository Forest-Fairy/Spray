package top.spray.engine.step.instance;

import top.spray.engine.step.executor.SprayProcessStepExecutor;
import top.spray.engine.base.instance.SprayInstanceStatus;

/**
 * the step result instance
 *  - create by an executor with the only instance
 */
public interface SprayStepResultInstance<Executor extends SprayProcessStepExecutor> {

    void create(Executor executor);

    void init();
    String id();
    /** 事务id */
    String transactionId();

    void setStatus(SprayInstanceStatus status);
    SprayInstanceStatus getStatus();
}
