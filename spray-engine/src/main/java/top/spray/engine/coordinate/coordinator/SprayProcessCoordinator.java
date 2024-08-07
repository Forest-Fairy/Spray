package top.spray.engine.coordinate.coordinator;

import top.spray.core.engine.execute.SprayClosable;
import top.spray.core.engine.execute.SprayListenable;
import top.spray.core.engine.execute.SprayMetaDrive;
import top.spray.core.engine.props.SprayData;
import top.spray.core.engine.types.coordinate.status.SprayCoordinatorStatus;
import top.spray.core.engine.types.data.dispatch.result.SprayEventDispatchResultStatus;
import top.spray.engine.coordinate.meta.SprayProcessCoordinatorMeta;
import top.spray.engine.event.handler.SprayCoordinatorEventHandler;
import top.spray.engine.event.model.SprayEvent;
import top.spray.engine.event.handler.SprayEventReceiver;
import top.spray.engine.prop.SprayVariableContainer;
import top.spray.engine.step.executor.SprayExecutorDefinition;

import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

/**
 * Define the coordinator of a process
 */
public interface SprayProcessCoordinator extends
        SprayMetaDrive<SprayProcessCoordinatorMeta>,
        Supplier<SprayCoordinatorStatus>, Runnable,
        SprayEventReceiver, SprayListenable<SprayProcessCoordinator, SprayCoordinatorEventHandler>, SprayClosable {
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

    ClassLoader getCreatorThreadClassLoader();

    int executorCount();


    @Override
    SprayProcessCoordinator addListener(SprayCoordinatorEventHandler listeners);

    @Override
    List<SprayCoordinatorEventHandler> getListeners();

    @Override
    void receive(SprayEvent event);

    //    /** a method for executor to publish its data */
//    void publishData(String variablesIdentityDataKey, SprayNextStepFilter stepFilter, SprayProcessStepExecutor fromExecutor, SprayData data, boolean still);


    /**
     * unit method for executing
     */
    void send(String curExecutorNameKey, SprayVariableContainer lastVariables, String fromExecutorNameKey, SprayData data, boolean still);


    /** the only way to get variablesContainer */
    SprayVariableContainer getVariablesContainer(String identityDataKey);

    /** the only way to get the executor */
    SprayExecutorDefinition getStepExecutor(String executorNameKey);

    /** all the recorded input data of the executor in coordinator */
    Set<String> getInputDataKeys(String executorNameKey);

    /** all the recorded output data of the executor in coordinator */
    Set<String> getOutputDataKeys(String executorNameKey);

    /** data dispatch results track */
    List<SprayEventDispatchResultStatus> getDispatchResults(String dataKey);
}
