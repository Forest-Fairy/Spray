package top.spray.engine.step.executor.transaction;

import top.spray.engine.step.executor.SprayProcessStepExecutor;

/**
 * SprayTransactionSupportExecutor
 *  - implement this interface to support transactional
 */
public interface SprayTransactionSupportExecutor extends SprayProcessStepExecutor {
    void commit();
    void rollback();
}
