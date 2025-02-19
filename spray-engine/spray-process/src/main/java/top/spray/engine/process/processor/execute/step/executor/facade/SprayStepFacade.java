package top.spray.engine.process.processor.execute.step.executor.facade;

import top.spray.core.dynamic.SprayClassLoader;
import top.spray.engine.process.infrastructure.analyse.SprayAnalysable;
import top.spray.engine.process.infrastructure.execute.SprayClosable;
import top.spray.engine.process.infrastructure.listen.SprayListenable;
import top.spray.engine.process.infrastructure.meta.SprayMetaDrive;
import top.spray.engine.process.processor.dispatch.coordinate.coordinator.SprayProcessCoordinator;
import top.spray.engine.process.processor.execute.step.meta.SprayExecutorType;
import top.spray.engine.process.processor.execute.step.meta.SprayOptionalData;
import top.spray.engine.process.processor.execute.step.meta.SprayProcessExecuteStepMeta;
import top.spray.engine.process.processor.execute.step.status.SprayStepStatusInstance;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

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

    SprayStepStatusInstance runningStatus();

    SprayExecutorType getExecutorType();

    void receive(@Nonnull String variableContainerIdentityDataKey,
                 @Nullable String fromExecutorNameKey,
                 @Nullable SprayOptionalData optionalData);

}
