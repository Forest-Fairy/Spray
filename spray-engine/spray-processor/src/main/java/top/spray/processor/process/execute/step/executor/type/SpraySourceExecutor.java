package top.spray.processor.process.execute.step.executor.type;

import top.spray.core.global.prop.SprayData;
import top.spray.core.global.stream.SprayDataIterator;
import top.spray.core.global.stream.SprayDataStream;
import top.spray.core.global.stream.SprayOptional;
import top.spray.processor.process.dispatch.coordinate.coordinator.SprayProcessCoordinator;
import top.spray.processor.process.execute.step.executor.SprayStepExecutor;
import top.spray.processor.process.execute.step.executor.facade.SprayStepFacade;
import top.spray.processor.process.execute.step.meta.SprayExecutorType;
import top.spray.processor.process.execute.step.meta.SprayOptionalData;
import top.spray.processor.process.execute.step.meta.SprayProcessExecuteStepMeta;

public abstract class SpraySourceExecutor extends SprayStepExecutor {

    public SpraySourceExecutor(SprayProcessCoordinator coordinator, SprayStepFacade executorFacade) {
        super(coordinator, executorFacade);
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
                        new SprayOptionalData(this.executorNameKey, next.get(), ! iterator.isDone()));
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
