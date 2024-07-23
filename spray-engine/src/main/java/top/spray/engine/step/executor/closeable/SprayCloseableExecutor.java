package top.spray.engine.step.executor.closeable;

import top.spray.core.engine.props.SprayData;
import top.spray.engine.exception.SprayExecuteException;
import top.spray.engine.prop.SprayVariableContainer;
import top.spray.engine.step.executor.SprayBaseStepExecutor;
import top.spray.engine.step.executor.SprayProcessStepExecutor;


public abstract class SprayCloseableExecutor extends SprayBaseStepExecutor {

    abstract boolean timeToClose(SprayVariableContainer variables, SprayProcessStepExecutor fromExecutor, SprayData data, boolean still);

    abstract void close(SprayVariableContainer variables, SprayProcessStepExecutor fromExecutor, SprayData data, boolean still);

    abstract void closeFailed(Throwable throwable);
    @Override
    protected void _execute(SprayVariableContainer variables, SprayProcessStepExecutor fromExecutor, SprayData data, boolean still) {
        _executeClosable(variables, fromExecutor, data, still);
        if (timeToClose(variables, fromExecutor, data, still)) {
            try {
                close();
            } catch (Throwable throwable) {
                closeFailed(new SprayExecuteException(
                        this, throwable, "close failed"));
            }
        }
    }

    abstract void _executeClosable(SprayVariableContainer variables, SprayProcessStepExecutor fromExecutor, SprayData data, boolean still);

}
