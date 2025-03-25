package top.spray.engine.process.processor.dispatch.coordinate.coordinator;

import top.spray.common.data.SprayData;
import top.spray.common.tools.loop.SprayLooper;
import top.spray.core.dynamic.SprayClassLoader;
import top.spray.engine.process.infrastructure.listen.SprayListener;
import top.spray.engine.process.processor.dispatch.coordinate.status.SprayCoordinatorStatusInstance;
import top.spray.engine.process.processor.data.manager.SprayVariableManager;
import top.spray.engine.process.processor.dispatch.exception.SprayCoordinatorDispatchErrorException;
import top.spray.engine.process.processor.dispatch.exception.SprayCoordinatorUnsupportedException;
import top.spray.engine.process.processor.dispatch.variables.SprayCoordinatorVariableManager;
import top.spray.engine.process.processor.execute.step.executor.SprayStepExecutor;
import top.spray.engine.process.processor.execute.step.meta.SprayOptionalData;
import top.spray.engine.process.processor.execute.step.meta.SprayProcessExecuteStepMeta;
import top.spray.engine.process.processor.execute.step.meta.SprayStepActiveType;
import top.spray.engine.process.processor.dispatch.coordinate.meta.SprayProcessCoordinatorMeta;
import top.spray.engine.process.processor.data.event.impl.SprayDataDispatchResultType;
import top.spray.engine.process.processor.dispatch.coordinate.status.SprayCoordinatorStatus;
import top.spray.engine.process.processor.dispatch.exception.SprayExecutorGenerateError;
import top.spray.engine.process.processor.dispatch.filters.SprayStepExecuteConditionFilter;
import top.spray.engine.process.processor.execute.step.executor.facade.SprayStepFacade;
import top.spray.engine.process.processor.execute.step.status.SprayStepStatus;

import java.util.*;
import java.util.concurrent.*;

public class SprayDefaultProcessCoordinator implements SprayProcessCoordinator {
    private final String transactionId;
    private final SprayProcessCoordinatorMeta coordinatorMeta;
    private final ClassLoader creatorThreadClassLoader;
    private final List<SprayListener> listeners;
    private final Map<String, SprayStepFacade> cachedExecutorMap;
    private final SprayCoordinatorStatusInstance coordinatorStatusInstance;
    private final SprayVariableManager variableManager;

    public SprayDefaultProcessCoordinator(String transactionId, SprayProcessCoordinatorMeta coordinatorMeta) {
        this.coordinatorMeta = coordinatorMeta;
        this.transactionId = transactionId;
        this.cachedExecutorMap = new ConcurrentHashMap<>();
        this.listeners = new LinkedList<>();
        this.creatorThreadClassLoader = Thread.currentThread().getContextClassLoader();
        this.coordinatorStatusInstance = new SprayCoordinatorStatusInstance(this);
        this.variableManager = new SprayCoordinatorVariableManager(this);
        initExecutors();
    }

    private void initExecutors() {
        createExecutors(this.getMeta().listStartNodes());
    }

    private void createExecutors(List<? extends SprayProcessExecuteStepMeta> nodes) {
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
    public String transactionId() {
        return this.transactionId;
    }

    @Override
    public final SprayProcessCoordinatorMeta getMeta() {
        return coordinatorMeta;
    }

    @Override
    public boolean listenerRegister(SprayListener listener) {
        if (listener.isForListenable(this) && !listeners.contains(listener)) {
            this.listeners.add(listener);
            return true;
        }
        return false;
    }

    @Override
    public SprayClassLoader getClassloader(String executorNameKey) {
        return getExecutorFacade(executorNameKey).getClassLoader();
    }

    @Override
    public List<SprayListener> listListeners() {
        return listeners;
    }

    @Override
    public SprayCoordinatorStatusInstance runningStatus() {
        return this.coordinatorStatusInstance;
    }

    @Override
    public boolean canDoStart() {
        return SprayCoordinatorStatus.INITIALIZING.equals(this.coordinatorStatusInstance.getStatus());
    }

    @Override
    public synchronized void start() {
        if (! this.canDoStart()) {
            throw new IllegalStateException(this.coordinatorStatusInstance.getStatus().typeName());
        }
        // TODO start
        this.coordinatorStatusInstance.setStatus(SprayCoordinatorStatus.RUNNING);
        List<SprayProcessExecuteStepMeta> startNodes = this.getMeta().listStartNodes();
        String processVariableContainerIdentityDataKey = this.variableManager.getProcessVariableContainer().identityDataKey();
        List<SprayData> defaultStartData = this.getMeta().getDefaultDataList();
        if (defaultStartData == null || defaultStartData.isEmpty()) {
            dispatchDataToNodes(startNodes, processVariableContainerIdentityDataKey, null, null);
        } else {
            Iterator<SprayData> it = defaultStartData.iterator();
            while (it.hasNext()) {
                SprayData data = it.next();
                dispatchDataToNodes(startNodes, processVariableContainerIdentityDataKey,
                        null, new SprayOptionalData(this.transactionId, data, it.hasNext()));
            }
        }
    }

    @Override
    public boolean canDoResume() {
        return false;
    }

    @Override
    public synchronized void resume() {
        throw new SprayCoordinatorUnsupportedException(this);
    }

    @Override
    public boolean canDoPause() {
        return false;
    }

    @Override
    public void pause() {
        throw new SprayCoordinatorUnsupportedException(this);
    }

    @Override
    public boolean canDoCancel() {
        return ! this.coordinatorStatusInstance.getStatus().isEnd();
    }

    @Override
    public void cancel() {
        this.shutdown();
    }

    protected void setDispatchResult(String variableContainerIdentityDataKey, String fromExecutorNameKey,
                                     SprayOptionalData optionalData, SprayProcessExecuteStepMeta nextMeta,
                                     SprayDataDispatchResultType dataDispatchStatus, Object... params) {
        this.getVariableManager().setDataDispatchResult(variableContainerIdentityDataKey, fromExecutorNameKey, optionalData, nextMeta, dataDispatchStatus, params);
    }


    @Override
    public void dispatchData(String variableContainerIdentityDataKey, SprayStepExecutor fromExecutor, SprayOptionalData optionalData, String toExecutorNameKeys) {
        String fromExecutorNameKey = fromExecutor.executorNameKey();
        if (toExecutorNameKeys == null) {
            setDispatchResult(variableContainerIdentityDataKey, fromExecutorNameKey,
                    optionalData, null, SprayDataDispatchResultType.FAILED,
                    "param is invalid, please contact the technician");
            return;
        }
        List<? extends SprayProcessExecuteStepMeta> nextNodes = this.listNextSteps(fromExecutorNameKey);
        if (!nextNodes.isEmpty() && ! toExecutorNameKeys.isBlank()) {
            nextNodes = nextNodes.stream()
                    .filter(nextNodeMeta -> {
                        if (! toExecutorNameKeys.contains(this.getExecutorNameKey(nextNodeMeta))) {
                            this.setDispatchResult(variableContainerIdentityDataKey, fromExecutorNameKey,
                                    optionalData, nextNodeMeta, SprayDataDispatchResultType.FILTERED);
                            return false;
                        }
                        return true;
                    })
                    .toList();
        }
        this.dispatchDataToNodes(nextNodes, variableContainerIdentityDataKey, fromExecutorNameKey, optionalData);
    }

    protected final void dispatchDataToNodes(List<? extends SprayProcessExecuteStepMeta> nodes, String variableContainerIdentityDataKey, String fromExecutorNameKey, SprayOptionalData optionalData) {
        if (nodes == null || nodes.isEmpty()) {
            this.setDispatchResult(variableContainerIdentityDataKey, fromExecutorNameKey, optionalData, null, SprayDataDispatchResultType.ABANDONED);
            return;
        }
        for (SprayProcessExecuteStepMeta nodeMeta : nodes) {
            if (this.runningStatus().getStatus().isEnd()) {
                continue;
            }
            if (! validateExecutorBeforeCreateOrExecute(variableContainerIdentityDataKey, fromExecutorNameKey, optionalData, nodeMeta)) {
                continue;
            }
            // run it
            this.doNodeDataDispatch(this.getExecutorFacade(this.getExecutorNameKey(nodeMeta)),
                    variableContainerIdentityDataKey, fromExecutorNameKey, optionalData);
        }
    }

    private boolean validateExecutorBeforeCreateOrExecute(String variableContainerIdentityDataKey, String fromExecutorNameKey, SprayOptionalData optionalData, SprayProcessExecuteStepMeta nodeMeta) {
        boolean create = true;
        if (! SprayStepActiveType.ACTIVE.equals(nodeMeta.stepActiveType())) {
            if (SprayStepActiveType.SKIP.equals(nodeMeta.stepActiveType())) {
                this.dispatchDataToNodes(nodeMeta.nextNodes(), variableContainerIdentityDataKey, fromExecutorNameKey, optionalData);
            }
            this.setDispatchResult(variableContainerIdentityDataKey, fromExecutorNameKey, optionalData, nodeMeta, SprayDataDispatchResultType.SKIPPED);
            create = false;
        } else {
            if (!nodeMeta.getExecuteConditionFilter().isEmpty()) {
                for (SprayStepExecuteConditionFilter filter : nodeMeta.getExecuteConditionFilter()) {
                    if (!filter.filterBeforeExecute(this, variableContainerIdentityDataKey, optionalData, nodeMeta)) {
                        create = false;
                        this.setDispatchResult(variableContainerIdentityDataKey, fromExecutorNameKey, optionalData, nodeMeta, SprayDataDispatchResultType.ABANDONED);
                        break;
                    }
                }
            }
        }
        return create;
    }

    protected final void doNodeDataDispatch(SprayStepFacade toExecutor, String variableContainerIdentityDataKey,
                                            String fromExecutorNameKey, SprayOptionalData optionalData) {
        try {
            int copyMode = toExecutor.getMeta().varCopyMode();
            if (copyMode == 0) {
                toExecutor.receive(variableContainerIdentityDataKey, fromExecutorNameKey, optionalData);
            } else {
                variableContainerIdentityDataKey = copyMode == 1
                        ? this.getVariableManager().easyCopyVariable(toExecutor.executorNameKey(), variableContainerIdentityDataKey, fromExecutorNameKey).identityDataKey()
                        : this.getVariableManager().deepCopyVariable(toExecutor.executorNameKey(), variableContainerIdentityDataKey, fromExecutorNameKey).identityDataKey();
                toExecutor.receive(variableContainerIdentityDataKey, fromExecutorNameKey, optionalData);
            }
            this.setDispatchResult(variableContainerIdentityDataKey, fromExecutorNameKey, optionalData, toExecutor.getMeta(), SprayDataDispatchResultType.SUCCESS);
        } catch (Exception e) {
            this.setDispatchResult(variableContainerIdentityDataKey, fromExecutorNameKey, optionalData, toExecutor.getMeta(), SprayDataDispatchResultType.FAILED, e);
        }
    }



    @Override
    public final SprayStepFacade getExecutorFacade(String executorNameKey) {
        return executorNameKey == null ? null : this.cachedExecutorMap.get(executorNameKey);
    }

    @Override
    public List<? extends SprayProcessExecuteStepMeta> listNextSteps(String executorNameKey) {
        return Optional.of(cachedExecutorMap.get(executorNameKey))
                .map(SprayStepFacade::getMeta)
                .map(SprayProcessExecuteStepMeta::nextNodes)
                .orElse(new ArrayList<>(0));
    }

    @Override
    public SprayVariableManager getVariableManager() {
        return this.variableManager;
    }

    @Override
    public void shutdown() {
        forceShutdown();
    }

    @Override
    public void forceShutdown() {
        SprayLooper.loop(this.cachedExecutorMap.entrySet(), (hasNext, entry) -> {
            entry.getValue().close();
        }, (entry, error) -> {
            SprayLooper.loopAndIgnoredException(entry.getValue().listListeners(), (still, listener) -> {
                // TODO handle exception with its handler
                throw new RuntimeException(error);
            });
        });
        if (this.creatorThreadClassLoader instanceof SprayClassLoader scl) {
            scl.close();
        }
    }
}
