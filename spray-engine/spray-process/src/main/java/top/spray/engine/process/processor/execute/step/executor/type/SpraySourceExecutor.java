package top.spray.engine.process.processor.execute.step.executor.type;

import top.spray.common.data.SprayData;
import top.spray.common.tools.SprayOptional;
import top.spray.core.stream.SprayDataIterator;
import top.spray.core.stream.SprayDataStream;
import top.spray.engine.process.processor.dispatch.coordinate.coordinator.SprayProcessCoordinator;
import top.spray.engine.process.processor.execute.step.executor.SprayStepExecutor;
import top.spray.engine.process.processor.execute.step.executor.facade.SprayStepExecutorOwner;
import top.spray.engine.process.processor.execute.step.executor.facade.SprayStepFacade;
import top.spray.engine.process.processor.execute.step.meta.SprayExecutorType;
import top.spray.engine.process.processor.execute.step.meta.SprayOptionalData;
import top.spray.engine.process.processor.execute.step.meta.SprayProcessExecuteStepMeta;

public abstract class SpraySourceExecutor extends SprayStepExecutor {

    public SpraySourceExecutor(SprayStepExecutorOwner executorOwner, String transactionId) {
        super(executorOwner, transactionId);
    }

    @Override
    public SprayExecutorType executorType() {
        return SprayExecutorType.SOURCE;
    }

    @Override
    protected void _execute(String fromExecutorNameKey, String variableContainerIdentityDataKey, SprayOptionalData optionalData) throws Exception {
        SprayDataIterator iterator = read(fromExecutorNameKey, variableContainerIdentityDataKey, optionalData)
                .cachedIterator(this.executorNameKey, (stream) -> createIteratorWithConfig(stream, this.stepMeta));
        while (iterator.hasNext()) {
            SprayOptional<SprayData> next = iterator.next();
            if (next.isPresent()) {
                this.publishData(variableContainerIdentityDataKey,
                        new SprayOptionalData(this.executorNameKey, next.orElse(null), ! iterator.isDone()));
            }
        }
    }

    /**
     * wrap the original iterator with some data iterate strategy, such as empty-blocking, batch-blocking, etc...
     */
    private SprayDataIterator createIteratorWithConfig(SprayDataStream dataStream, SprayProcessExecuteStepMeta stepMeta) {

    }

    protected abstract SprayDataStream read(String fromExecutorNameKey, String variableContainerIdentityDataKey, SprayOptionalData optionalData);

}
