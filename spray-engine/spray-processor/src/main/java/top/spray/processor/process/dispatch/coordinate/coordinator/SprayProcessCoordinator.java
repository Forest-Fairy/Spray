package top.spray.processor.process.dispatch.coordinate.coordinator;

import top.spray.core.system.dynamic.SprayClassLoader;
import top.spray.processor.infrustructure.execute.SprayClosable;
import top.spray.processor.infrustructure.execute.SprayRunnable;
import top.spray.processor.infrustructure.listen.SprayListenable;
import top.spray.processor.infrustructure.listen.SprayListener;
import top.spray.processor.infrustructure.meta.SprayMetaDrive;
import top.spray.processor.process.data.manager.SprayVariableManager;
import top.spray.processor.process.dispatch.coordinate.meta.SprayProcessCoordinatorMeta;
import top.spray.processor.process.dispatch.coordinate.status.SprayCoordinatorStatusInstance;
import top.spray.processor.process.execute.step.executor.SprayStepExecutor;
import top.spray.processor.process.execute.step.executor.facade.SprayStepFacade;
import top.spray.processor.process.execute.step.meta.SprayOptionalData;
import top.spray.processor.process.execute.step.meta.SprayProcessExecuteStepMeta;

import java.util.List;

/**
 * Define the coordinator of a process
 */
public interface SprayProcessCoordinator extends
        SprayMetaDrive<SprayProcessCoordinatorMeta>, SprayListenable,
        SprayRunnable<SprayCoordinatorStatusInstance>, SprayClosable {


    @Override
    SprayProcessCoordinatorMeta getMeta();

    @Override
    String transactionId();

    default String name() {
        return String.format("[%s]%s(%s)", transactionId(), getMeta().getName(), getMeta().getId());
    }

    /**
     * every executor's classloader should be managed by the coordinator
     * @param executorNameKey executorNameKey
     * @return classloader which load the executor's class
     */
    SprayClassLoader getClassloader(String executorNameKey);

    @Override
    boolean listenerRegister(SprayListener listener);

    /**
     * method for publishing data to executor
     * @param toExecutorNameKeys blank but not empty means not run, empty or null means run all
     */
    void dispatchData(String variableContainerIdentityDataKey, SprayStepExecutor fromExecutor, SprayOptionalData optionalData, String toExecutorNameKeys);


    /** method for getting the executor's facade */
    SprayStepFacade getExecutorFacade(String executorNameKey);

    /** method for listing the next steps' meta */
    List<SprayProcessExecuteStepMeta> listNextSteps(String executorNameKey);

    /** method for getting the variable manager */
    SprayVariableManager getVariableManager();

    @Override
    SprayCoordinatorStatusInstance runningStatus();

    @Override
    void startOrContinue();

    @Override
    void pause();

    @Override
    void cancel();

    @Override
    void shutdown();

    @Override
    void forceShutdown();

    default String getExecutorNameKey(SprayProcessExecuteStepMeta stepMeta) {
        return String.format("%s-%s[%s](%s)", transactionId(), stepMeta.getExecutorType().name(), stepMeta.getId(), stepMeta.getName());
    }

    //    /** all the recorded input data of the executor in coordinator */
//    Set<String> getInputDataKeys(String executorNameKey);

//    /** all the recorded output data of the executor in coordinator */
//    Set<String> getOutputDataKeys(String executorNameKey);

//    /** data dispatch results track */
//    Iterable<SprayDataDispatchResultEventType> getDispatchResults(String dataKey);
}
