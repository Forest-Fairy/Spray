package top.spray.processor.process.execute.step.executor.support;

import top.spray.processor.process.execute.step.meta.SprayOptionalData;

/**
 * SprayTransactionSupportExecutor
 *  - implement this interface to support transactional
 */
public interface SprayExecutorTransactionSupport {

    boolean timeToCommit(String fromExecutorNameKey, String variableContainerIdentityDataKey, SprayOptionalData optionalData);

    void commit(String fromExecutorNameKey, String variableContainerIdentityDataKey, SprayOptionalData optionalData);

    void rollback(String fromExecutorNameKey, String variableContainerIdentityDataKey, SprayOptionalData optionalData);

}
