package top.spray.engine.coordinate.coordinator;

import top.spray.core.engine.execute.SprayClosable;
import top.spray.core.engine.execute.SprayMetaDrive;
import top.spray.core.engine.props.SprayData;
import top.spray.core.engine.types.coordinate.status.SprayCoordinatorStatus;
import top.spray.core.engine.types.data.dispatch.result.SprayDataDispatchResultStatus;
import top.spray.engine.coordinate.meta.SprayProcessCoordinatorMeta;
import top.spray.engine.prop.SprayVariableContainer;
import top.spray.engine.step.executor.SprayProcessStepExecutor;
import top.spray.engine.step.condition.SprayNextStepFilter;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

/**
 * Define the coordinator of a process
 */
public interface SprayProcessCoordinator extends
        SprayMetaDrive<SprayProcessCoordinatorMeta>,
        Supplier<SprayCoordinatorStatus>, Runnable,
        SprayClosable {
    @Override
    SprayProcessCoordinatorMeta getMeta();

    /** a method for completable future */
    @Override
    default SprayCoordinatorStatus get() {
        this.run();
        // this method run after the run method so that can get the status status.
        return status();
    }
    /** the execution method */
    @Override
    void run();

    /** the coordinator's status: none-blocked method  */
    SprayCoordinatorStatus status();

    default String getExecutorNameKey(SprayProcessStepExecutor executor) {
        return executor.getMeta().getExecutorNameKey(this.getMeta());
    }

    ClassLoader getCreatorThreadClassLoader();

    /** the only way to get variablesContainer */
    SprayVariableContainer getVariablesContainer(String identityDataKey);

    /** the only way to get the executor */
    SprayProcessStepExecutor getStepExecutor(String executorNameKey);

    int createExecutorCount();

    /** a method for executor to publish its data */
    void dispatch(String variablesIdentityDataKey, SprayNextStepFilter stepFilter, SprayProcessStepExecutor fromExecutor, SprayData data, boolean still);

    List<SprayDataDispatchResultStatus> getDispatchResults(String dataKey);

    /**
     * unit method for executing
     */
    void executeNext(SprayProcessStepExecutor nextStepExecutor, SprayVariableContainer lastVariables, SprayProcessStepExecutor fromExecutor, SprayData data, boolean still);


    /** all the executor in coordinator */
    Map<String, SprayProcessStepExecutor> getCachedExecutorMap();

    /** all the recorded input data of the executor in coordinator */
    Set<String> getInputDataKeys(String executorNameKey);

    /** all the recorded output data of the executor in coordinator */
    Set<String> getOutputDataKeys(String executorNameKey);

}
