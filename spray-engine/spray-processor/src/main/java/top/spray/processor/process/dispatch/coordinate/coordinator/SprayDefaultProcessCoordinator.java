package top.spray.processor.process.dispatch.coordinate.coordinator;

import top.spray.processor.process.dispatch.coordinate.status.SprayCoordinatorStatusInstance;
import top.spray.processor.process.data.manager.SprayVariableManager;
import top.spray.processor.process.execute.step.meta.SprayOptionalData;
import top.spray.processor.process.execute.step.meta.SprayProcessExecuteStepMeta;
import top.spray.processor.process.execute.step.meta.SprayStepActiveType;
import top.spray.core.global.prop.SprayData;
import top.spray.processor.process.dispatch.coordinate.meta.SprayProcessCoordinatorMeta;
import top.spray.processor.process.data.event.impl.SprayDataDispatchResultType;
import top.spray.processor.process.dispatch.coordinate.status.SprayCoordinatorStatus;
import top.spray.core.system.dynamic.SprayClassLoader;
import top.spray.processor.exception.coordinate.SprayExecutorGenerateError;
import top.spray.processor.process.dispatch.filters.SprayStepExecuteConditionFilter;
import top.spray.processor.process.execute.step.executor.facade.SprayStepFacade;

import java.util.*;
import java.util.concurrent.*;

public class SprayDefaultProcessCoordinator implements SprayProcessCoordinator {
    private final String transactionId;
    private final SprayProcessCoordinatorMeta coordinatorMeta;
    private final ClassLoader creatorThreadClassLoader;
    private final List<SprayCoordinatorListener<? extends SprayCoordinatorListener<?>>> listeners;
    private final Map<String, SprayStepFacade> cachedExecutorMap;
    private final SprayCoordinatorStatusInstance coordinatorStatusInstance;

    public SprayDefaultProcessCoordinator(String transactionId, SprayProcessCoordinatorMeta coordinatorMeta) {
        this.coordinatorMeta = coordinatorMeta;
        this.transactionId = transactionId;
        this.cachedExecutorMap = new ConcurrentHashMap<>();
        this.listeners = new LinkedList<>();
        this.creatorThreadClassLoader = Thread.currentThread().getContextClassLoader();
        this.coordinatorStatusInstance = new SprayCoordinatorStatusInstance(this.coordinatorMeta);
        initExecutors();
    }

    private void initExecutors() {
        createExecutors(this.getMeta().listStartNodes());
    }

    private void createExecutors(List<SprayProcessExecuteStepMeta> nodes) {
        nodes.forEach(node -> {
            String executorNameKey = this.getExecutorNameKey(node);
            if (this.cachedExecutorMap.get(executorNameKey) == null) {
                this.cachedExecutorMap.put(executorNameKey, createStepExecutor(node));
            }
            if (node.nextNodes() != null && !node.nextNodes().isEmpty()) {
                createExecutors(node.nextNodes());
            }
        });
    }

    private SprayStepFacade createStepExecutor(SprayProcessExecuteStepMeta executorMeta) {
        try {
            // all executors' classloader should be created with the same classloader
            // which create the coordinator
            return SprayExecutorFactory.create(this, executorMeta, true);
        } catch (Throwable e) {
            throw new SprayExecutorGenerateError(this.getMeta(), executorMeta, e);
        }
    }

    @Override
    public final SprayProcessCoordinatorMeta getMeta() {
        return coordinatorMeta;
    }

    @Override
    public final ClassLoader getClassloader() {
        return this.creatorThreadClassLoader;
    }

    @Override
    public final List<SprayCoordinatorListener<? extends SprayCoordinatorListener<?>>> getListeners() {
        return this.listeners;
    }

    @Override
    public final SprayCoordinatorStatusInstance getCoordinatorStatusInstance() {
        return this.coordinatorStatusInstance;
    }


    @Override
    public void start() {
        // TODO start receiving
    }


    protected void setDispatchResult(String variableContainerIdentityDataKey, String fromExecutorNameKey,
                                     SprayOptionalData optionalData, SprayProcessExecuteStepMeta nextMeta,
                                     SprayDataDispatchResultType dataDispatchStatus) {
        this.getVariableManager().setDataDispatchResult(variableContainerIdentityDataKey, fromExecutorNameKey, optionalData, nextMeta, dataDispatchStatus);
    }


    @Override
    public void dispatchData(String toExecutorNameKeys, String variableContainerIdentityDataKey,
                             String fromExecutorNameKey, SprayData data, boolean still) {
        if (toExecutorNameKeys.trim().isEmpty() && !toExecutorNameKeys.isEmpty()) {
            // not run
            return;
        }
        List<SprayProcessExecuteStepMeta> nextNodes = this.listNextSteps(fromExecutorNameKey);
        if (!nextNodes.isEmpty() && toExecutorNameKeys != null && ! toExecutorNameKeys.isBlank()) {
            nextNodes = nextNodes.stream()
                    .filter(nextNodeMeta -> toExecutorNameKeys.contains(this.getExecutorNameKey(nextNodeMeta)))
                    .toList();
        }
        this.dispatchDataToNodes(nextNodes, variableContainerIdentityDataKey, fromExecutorNameKey, data, still);
    }

    private boolean validateExecutorBeforeCreate(String variableContainerIdentityDataKey, SprayOptionalData optionalData, SprayProcessExecuteStepMeta nodeMeta) {
        boolean create = true;
        if (! SprayStepActiveType.ACTIVE.equals(nodeMeta.stepActiveType())) {
            if (SprayStepActiveType.SKIP.equals(nodeMeta.stepActiveType())) {
                this.dispatchDataToNodes(nodeMeta.nextNodes(), variableContainerIdentityDataKey, fromExecutorNameKey, data, still);
            }
            this.setDispatchResult(variableContainerIdentityDataKey, fromExecutorNameKey, data, still, nodeMeta, SprayDataDispatchResultType.SKIPPED);
            create = false;
        } else {
            if (!nodeMeta.getExecuteConditionFilter().isEmpty()) {
                for (SprayStepExecuteConditionFilter filter : nodeMeta.getExecuteConditionFilter()) {
                    if (!filter.filterBeforeExecute(this, variableContainerIdentityDataKey, optionalData, nodeMeta)) {
                        create = false;
                        this.setDispatchResult(variableContainerIdentityDataKey, fromExecutorNameKey, data, still, nodeMeta, SprayDataDispatchResultType.ABANDONED);
                        break;
                    }
                }
            }
        }
        return create;
    }

    protected final void dispatchDataToNodes(List<SprayProcessExecuteStepMeta> nodes, String variableContainerIdentityDataKey, String fromExecutorNameKey, SprayOptionalData optionalData) {
        if (nodes == null || nodes.isEmpty()) {
            this.setDispatchResult(variableContainerIdentityDataKey, fromExecutorNameKey, optionalData, null, SprayDataDispatchResultType.ABANDONED);
            return;
        }
        for (SprayProcessExecuteStepMeta nodeMeta : nodes) {
            if (! validCoordinatorStatus()) {
                continue;
            }
            if (! validateExecutorBeforeCreate(variableContainerIdentityDataKey, fromExecutorNameKey, data, still, nodeMeta)) {
                continue;
            }
            // run it
            this.doNodeDataDispatch(this.getExecutorFacade(this.getExecutorNameKey(nodeMeta)),
                    variableContainerIdentityDataKey, fromExecutorNameKey, data, still);
        }
    }

    protected final void doNodeDataDispatch(SprayStepFacade toExecutor, String variableContainerIdentityDataKey,
                                            String fromExecutorNameKey, SprayData data, boolean still) {
        try {
            int copyMode = toExecutor.getMeta().varCopyMode();
            if (copyMode != 0) {
                toExecutor.receive(variableContainerIdentityDataKey, fromExecutorNameKey, data, still);
            } else {
                toExecutor.receive(copyMode == 1 ?
                        this.getVariableManager().easyCopyVariable(toExecutor.executorNameKey(), variableContainerIdentityDataKey, fromExecutorNameKey).identityDataKey() :
                        this.getVariableManager().deepCopyVariable(toExecutor.executorNameKey(), variableContainerIdentityDataKey, fromExecutorNameKey).identityDataKey(),
                        fromExecutorNameKey, data, still);
            }
            this.setDispatchResult(variableContainerIdentityDataKey, fromExecutorNameKey, data, still, toExecutor.getMeta(), SprayDataDispatchResultType.SUCCESS);
        } catch (Exception e) {
            this.setDispatchResult(variableContainerIdentityDataKey, fromExecutorNameKey, data, still, toExecutor.getMeta(), SprayDataDispatchResultType.FAILED);
        }
    }

    private boolean validCoordinatorStatus() {
        return SprayCoordinatorStatus.RUNNING.equals(this.getCoordinatorStatusInstance().getStatus());
    }



    @Override
    public final SprayStepFacade getExecutorFacade(String executorNameKey) {
        return executorNameKey == null ? null : this.cachedExecutorMap.get(executorNameKey);
    }

    @Override
    public List<SprayProcessExecuteStepMeta> listNextSteps(String executorNameKey) {
        return Optional.of(cachedExecutorMap.get(executorNameKey))
                .map(SprayStepFacade::getMeta)
                .map(SprayProcessExecuteStepMeta::nextNodes)
                .orElse(new ArrayList<>(0));
    }

    @Override
    public SprayVariableManager getVariableManager() {

    }
    @Override
    public void shutdown() {
        if (this.getClassloader() instanceof SprayClassLoader scl) {
            scl.closeInRuntime();
        }
        for (Map.Entry<String, SprayStepFacade> executorEntry : this.cachedExecutorMap.entrySet()) {
            try {
                executorEntry.getValue().close();
            } catch (Exception e) {
                // TODO handle exception with its handler
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void forceShutdown() {

    }
}
