package top.spray.engine.step.executor.transaction;

import top.spray.core.engine.props.SprayData;
import top.spray.engine.prop.SprayVariableContainer;
import top.spray.engine.step.executor.SprayDefaultStepExecutorDefinition;
import top.spray.engine.step.executor.SprayExecutorDefinition;

/**
 * SprayTransactionSupportExecutor
 *  - implement this interface to support transactional
 */
public abstract class SprayTransactionSupportExecutor extends SprayDefaultStepExecutorDefinition {
    protected abstract boolean timeToCommit(SprayVariableContainer variables, SprayExecutorDefinition fromExecutor, SprayData data, boolean still);
    protected abstract void commit();
    protected abstract void rollback();
}
