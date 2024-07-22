package top.spray.engine.step.executor.transaction;

import top.spray.core.engine.props.SprayData;
import top.spray.engine.prop.SprayVariableContainer;
import top.spray.engine.step.executor.SprayBaseStepExecutor;
import top.spray.engine.step.executor.SprayProcessStepExecutor;

/**
 * SprayTransactionSupportExecutor
 *  - implement this interface to support transactional
 */
public abstract class SprayTransactionSupportExecutor extends SprayBaseStepExecutor {
    protected abstract boolean timeToCommit(SprayVariableContainer variables, SprayProcessStepExecutor fromExecutor, SprayData data, boolean still);
    protected abstract void commit();
    protected abstract void rollback();
}
