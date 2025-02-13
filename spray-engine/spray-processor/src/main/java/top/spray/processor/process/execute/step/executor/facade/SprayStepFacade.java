package top.spray.processor.process.execute.step.executor.facade;

import top.spray.processor.infrustructure.analyse.SprayAnalysable;
import top.spray.processor.infrustructure.execute.SprayClosable;
import top.spray.processor.infrustructure.listen.SprayListenable;
import top.spray.processor.infrustructure.meta.SprayMetaDrive;
import top.spray.core.system.dynamic.SprayClassLoader;
import top.spray.processor.process.dispatch.coordinate.coordinator.SprayProcessCoordinator;
import top.spray.processor.process.execute.step.meta.SprayExecutorType;
import top.spray.processor.process.execute.step.meta.SprayOptionalData;
import top.spray.processor.process.execute.step.meta.SprayProcessExecuteStepMeta;
import top.spray.processor.process.execute.step.status.SprayStepStatusInstance;

/**
 * Define the executor of a process node
 */
public interface SprayStepFacade extends
        SprayMetaDrive<SprayProcessExecuteStepMeta>, SprayClosable,
        SprayListenable, SprayAnalysable {
    default String executorNameKey() {
        return getCoordinator().getExecutorNameKey(getMeta());
    }

    @Override
    default String transactionId() {
        return this.getCoordinator().transactionId();
    }

    @Override
    SprayProcessExecuteStepMeta getMeta();

    SprayClassLoader getClassLoader();

    SprayProcessCoordinator getCoordinator();

    SprayStepStatusInstance getStepStatus();

    SprayExecutorType getExecutorType();

    void receive(String variableContainerIdentityDataKey, String fromExecutorNameKey, SprayOptionalData optionalData);

}
