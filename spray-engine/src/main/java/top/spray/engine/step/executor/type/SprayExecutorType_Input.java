package top.spray.engine.step.executor.type;

import top.spray.core.engine.props.SprayData;
import top.spray.engine.prop.SprayVariableContainer;
import top.spray.engine.step.executor.SprayDefaultStepExecutorDefinition;
import top.spray.engine.step.executor.SprayExecutorDefinition;

public abstract class SprayExecutorType_Input extends SprayDefaultStepExecutorDefinition {

    protected boolean passDataToNext;

    @Override
    protected void initOnlyAtCreate0() {
        super.initOnlyAtCreate0();
        this.passDataToNext = Boolean.parseBoolean(this.getMeta().getString("passDataToNext", "false"));
    }

    /**
     * need pass the data to next executor
     * @return true by default
     */
    protected boolean passDataToNext() {
        return passDataToNext;
    }

    @Override
    protected void _execute(SprayVariableContainer variables, SprayExecutorDefinition fromExecutor, SprayData data, boolean still) {
//        super._execute(variables, fromExecutor, data, still);
        if (passDataToNext()) {
            publishData(variables, data, still);
        }
        doReadIn(variables, fromExecutor, still);
    }
    protected abstract void doReadIn(
            SprayVariableContainer variables, SprayExecutorDefinition fromExecutor, boolean still);
}
