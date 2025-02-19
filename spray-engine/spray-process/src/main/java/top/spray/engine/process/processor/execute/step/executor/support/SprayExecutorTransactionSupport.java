package top.spray.engine.process.processor.execute.step.executor.support;

import top.spray.engine.process.processor.execute.step.meta.SprayOptionalData;

/**
 * SprayTransactionSupportExecutor
 *  - implement this interface to support transactional
 */
public interface SprayExecutorTransactionSupport {

    boolean timeToCommit(String fromExecutorNameKey, String variableContainerIdentityDataKey, SprayOptionalData optionalData);

    void commit(String fromExecutorNameKey, String variableContainerIdentityDataKey, SprayOptionalData optionalData);

    void rollback(String fromExecutorNameKey, String variableContainerIdentityDataKey, SprayOptionalData optionalData);

}
