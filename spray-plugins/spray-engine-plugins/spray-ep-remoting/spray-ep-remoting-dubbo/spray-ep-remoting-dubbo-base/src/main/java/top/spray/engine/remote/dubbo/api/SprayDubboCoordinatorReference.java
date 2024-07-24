package top.spray.engine.remote.dubbo.api;

import top.spray.core.engine.props.SprayData;
import top.spray.core.engine.status.impl.SprayCoordinateStatus;
import top.spray.engine.prop.SprayVariableContainer;
import top.spray.engine.step.condition.SprayNextStepFilter;
import top.spray.engine.step.executor.SprayProcessStepExecutor;

public interface SprayDubboCoordinatorReference {
    String getCoordinatorStatus();

    int createExecutorCount();

    /** a method for executor to publish its data */
    void dispatch(String variablesIdentityDataKey, String fromExecutorNameKey, String data, boolean still);

}