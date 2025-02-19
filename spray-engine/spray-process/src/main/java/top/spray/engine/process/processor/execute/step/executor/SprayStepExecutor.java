package top.spray.engine.process.processor.execute.step.executor;

import org.slf4j.MDC;
import top.spray.engine.process.infrastructure.analyse.SprayAnalysable;
import top.spray.engine.process.infrastructure.meta.SprayMetaDrive;
import top.spray.engine.process.infrastructure.prop.SprayVariableContainer;
import top.spray.engine.process.processor.dispatch.coordinate.coordinator.SprayProcessCoordinator;
import top.spray.engine.process.processor.execute.filters.SprayNextStepFilter;
import top.spray.engine.process.processor.execute.step.executor.facade.SprayStepFacade;
import top.spray.engine.process.processor.execute.step.meta.SprayExecutorType;
import top.spray.engine.process.processor.execute.step.meta.SprayOptionalData;
import top.spray.engine.process.processor.execute.step.meta.SprayProcessExecuteStepMeta;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public abstract class SprayStepExecutor implements SprayAnalysable, SprayMetaDrive<SprayProcessExecuteStepMeta> {
    protected final SprayProcessCoordinator coordinator;
    protected final SprayStepFacade executorFacade;
    protected final SprayProcessExecuteStepMeta stepMeta;
    protected final String transactionId;
    protected final String executorNameKey;

    public SprayStepExecutor(SprayProcessCoordinator coordinator, SprayStepFacade executorFacade) {
        this.coordinator = coordinator;
        this.executorFacade = executorFacade;
        this.transactionId = coordinator.transactionId();
        this.stepMeta = executorFacade.getMeta();
        this.executorNameKey = executorFacade.executorNameKey();
    }

    public SprayProcessCoordinator coordinator() {
        return this.coordinator;
    }

    @Override
    public SprayProcessExecuteStepMeta getMeta() {
        return stepMeta;
    }

    @Override
    public String transactionId() {
        return this.transactionId;
    }

    public String executorNameKey() {
        return executorNameKey;
    }

    public final Map<String, Object> execute(String fromExecutorNameKey, String variableContainerIdentityDataKey, SprayOptionalData optionalData) throws Exception {
        MDC.put("transactionId", this.transactionId);
        MDC.put("executorNameKey", this.executorNameKey);
        return this.execute0(fromExecutorNameKey, variableContainerIdentityDataKey, optionalData);
    }
    protected Map<String, Object> execute0(String fromExecutorNameKey, String variableContainerIdentityDataKey, SprayOptionalData optionalData) throws Exception {
        this._execute(fromExecutorNameKey, variableContainerIdentityDataKey, optionalData);
        return null;
    }
    protected abstract void _execute(String fromExecutorNameKey, String variableContainerIdentityDataKey, SprayOptionalData optionalData) throws Exception;

    /* it means not to execute with work thread if the return is null */
    public final String beforeExecute(String fromExecutorNameKey, String variableContainerIdentityDataKey, SprayOptionalData optionalData) {
        return _beforeExecute(fromExecutorNameKey, variableContainerIdentityDataKey, optionalData);
    }

    protected String _beforeExecute(String fromExecutorNameKey, String variableContainerIdentityDataKey, SprayOptionalData optionalData) {
        return "";
    }

    /* it means failed if the return is null */
    public final String afterExecute(String fromExecutorNameKey, String variableContainerIdentityDataKey, SprayOptionalData optionalData, Map<String, Object> executeInfo) {
        return _afterExecute(fromExecutorNameKey, variableContainerIdentityDataKey, optionalData,
                String.valueOf(executeInfo.get("isErrored")).equals("true"), executeInfo);
    }
    protected String _afterExecute(String fromExecutorNameKey, String variableContainerIdentityDataKey, SprayOptionalData optionalData, boolean isErrored, Map<String, Object> executeInfo) {
        return "";
    }

    public final Throwable ifErrored(String fromExecutorNameKey, String variableContainerIdentityDataKey, SprayOptionalData optionalData, Throwable t) {
        return _ifErrored(fromExecutorNameKey, variableContainerIdentityDataKey, optionalData, t);
    }
    protected Throwable _ifErrored(String fromExecutorNameKey, String variableContainerIdentityDataKey, SprayOptionalData optionalData, Throwable t) {
        return t;
    }

    protected void publishData(String variableContainerIdentityDataKey, SprayOptionalData optionalData) {
        publishData(null, variableContainerIdentityDataKey, optionalData);
    }

    protected void publishData(SprayNextStepFilter filter, String variableContainerIdentityDataKey, SprayOptionalData optionalData) {
        String not_publish = " ";
        String toExecutorNameKeys = "";
        if (filter != null) {
            List<SprayProcessExecuteStepMeta> nextStepMetas = this.coordinator.listNextSteps(this.executorNameKey);
            if (nextStepMetas != null) {
                for (SprayProcessExecuteStepMeta nextStepMeta : nextStepMetas) {
                    if (filter.filterBeforeDispatch(this, variableContainerIdentityDataKey, optionalData, nextStepMeta)) {
                        toExecutorNameKeys += "," + this.coordinator.getExecutorNameKey(nextStepMeta);
                    }
                }
            }
            if (toExecutorNameKeys.isEmpty()) {
                toExecutorNameKeys = not_publish;
            } else {
                toExecutorNameKeys = toExecutorNameKeys.substring(1);
            }
        }
        if (toExecutorNameKeys.equals(not_publish)) {
            return;
        }
        this.coordinator.dispatchData(variableContainerIdentityDataKey, this, optionalData, toExecutorNameKeys);
    }

    protected SprayVariableContainer getVariableContainer(String variableContainerIdentityDataKey) {
        return this.coordinator.getVariableManager().getVariableContainer(variableContainerIdentityDataKey);
    }
    protected SprayStepFacade getExecutorFacade(String executorNameKey) {
        Objects.requireNonNull(executorNameKey, "executorNameKey can not be null");
        return this.coordinator.getExecutorFacade(executorNameKey);
    }
    protected SprayStepFacade getMyFacade() {
        return this.getExecutorFacade(this.executorNameKey);
    }

    public abstract SprayExecutorType executorType();

    public void infoMerge(Map<String, Object> executeInfo, Map<String, Object> newExecuteInfo) {
        executeInfo.putAll(newExecuteInfo);
    }
}
