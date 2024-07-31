package top.spray.engine.coordinate.coordinator;

import cn.hutool.core.collection.CollUtil;
import top.spray.core.engine.exception.SprayNotSupportError;
import top.spray.core.engine.execute.SprayListenable;
import top.spray.core.engine.execute.SprayStepActiveType;
import top.spray.core.engine.props.SprayData;
import top.spray.core.engine.types.SprayType;
import top.spray.core.engine.types.data.dispatch.result.SprayDataDispatchResultStatus;
import top.spray.core.engine.types.step.status.SprayStepStatus;
import top.spray.core.engine.types.coordinate.status.SprayCoordinatorStatus;
import top.spray.core.dynamic.loader.SprayClassLoader;
import top.spray.engine.coordinate.handler.result.SprayDataDispatchResultHandler;
import top.spray.engine.event.model.SprayEvent;
import top.spray.engine.event.model.coordinate.SprayUnknownEvent;
import top.spray.engine.event.util.SpraySubscribes;
import top.spray.engine.exception.SprayExecutorGenerateError;
import top.spray.engine.factory.SprayExecutorFactory;
import top.spray.engine.prop.SprayCoordinatorVariableContainer;
import top.spray.engine.prop.SprayExecutorVariableContainer;
import top.spray.engine.prop.SprayVariableContainer;
import top.spray.engine.step.condition.SprayStepExecuteConditionFilter;
import top.spray.engine.event.model.execute.step.SprayReceiveDataEvent;
import top.spray.engine.event.handler.SprayCoordinatorEventHandler;
import top.spray.engine.coordinate.meta.SprayProcessCoordinatorMeta;
import top.spray.engine.step.executor.SprayProcessStepExecutor;
import top.spray.engine.step.condition.SprayNextStepFilter;
import top.spray.engine.step.meta.SprayProcessStepMeta;

import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.*;

public class SprayDefaultProcessCoordinator implements
        SprayProcessCoordinator, SprayListenable<SprayDefaultProcessCoordinator, SprayCoordinatorEventHandler>{
    private final SprayProcessCoordinatorMeta coordinatorMeta;
    private final List<SprayCoordinatorEventHandler> handlers;
    private final SprayVariableContainer defaultVariables;
    private final List<SprayData> defaultDataList;
    private final SprayDataDispatchResultHandler dispatchResultHandler;
    private final ClassLoader creatorThreadClassLoader;
    private final Map<String, SprayProcessStepExecutor> cachedExecutorMap;
    /** a namespace for executor's process data */
    private final Map<String, SprayVariableContainer> executorVariablesNamespace;
    private final Map<String, Set<String>> inputDataKeys;
    private final Map<String, Set<String>> outputDataKeys;
    /** event methods belong the coordinator */
    private final Map<String, List<Method>> coordinateEventMethods;
    private boolean executeOnlyOnce = true;

    public SprayDefaultProcessCoordinator(SprayProcessCoordinatorMeta coordinatorMeta) {
        this.cachedExecutorMap = new ConcurrentHashMap<>();
        this.executorVariablesNamespace = new ConcurrentHashMap<>();
        this.coordinatorMeta = coordinatorMeta;
        this.handlers = new ArrayList<>();
        this.defaultVariables = SprayCoordinatorVariableContainer.create(this);
        this.defaultDataList = new ArrayList<>();
        this.creatorThreadClassLoader = Thread.currentThread().getContextClassLoader();
        this.dispatchResultHandler = SprayDataDispatchResultHandler.get(coordinatorMeta);
        this.inputDataKeys = new ConcurrentHashMap<>();
        this.outputDataKeys = new ConcurrentHashMap<>();
        this.coordinateEventMethods = new HashMap<>();
        init();
    }

    private void init() {
        // don't let the coordinator run twice so that we could clear the default data list when we run
        if (!executeOnlyOnce) {
            throw new SprayNotSupportError(this.coordinatorMeta,
                    new IllegalAccessError("the coordinator can only be run once"));
        }
        executeOnlyOnce = false;
        initVar();
        initExecutors();
    }

    private void initVar() {
        if (this.coordinatorMeta.getDefaultDataList() != null) {
            this.defaultDataList.addAll(this.coordinatorMeta.getDefaultDataList());
        }
        SpraySubscribes.readSubscribeMethodsOnClass(this.coordinateEventMethods, this.getClass());
    }

    private void initExecutors() {
        createExecutors(this.getMeta().getStartNodes());
    }
    private void createExecutors(List<SprayProcessStepMeta> nodes) {
        nodes.forEach(node -> {
            String executorNameKey = node.getExecutorNameKey(this.getMeta());
            if (this.cachedExecutorMap.get(executorNameKey) == null) {
                this.cachedExecutorMap.put(executorNameKey, createStepExecutor(node));
            }
            if (node.nextNodes() != null && !node.nextNodes().isEmpty()) {
                createExecutors(node.nextNodes());
            }
        });
    }

    private SprayProcessStepExecutor createStepExecutor(SprayProcessStepMeta executorMeta) {
        try {
            // all executors' classloader should be created with the same classloader
            // which create the coordinator
            return SprayExecutorFactory.create(this, executorMeta, true);
        } catch (Throwable e) {
            throw new SprayExecutorGenerateError(this.getMeta(), executorMeta, e);
        }
    }


    @Override
    public final SprayDefaultProcessCoordinator addListener(SprayCoordinatorEventHandler listener) {
        this.listeners.add(listener);
        return this;
    }

    @Override
    public final SprayProcessCoordinatorMeta getMeta() {
        return coordinatorMeta;
    }


    @Override
    public final List<SprayCoordinatorEventHandler> getListeners() {
        return this.handlers;
    }

    @Override
    public final ClassLoader getCreatorThreadClassLoader() {
        return this.creatorThreadClassLoader;
    }

    @Override
    public final int executorCount() {
        return this.cachedExecutorMap.size();
    }

    @Override
    public final SprayCoordinatorStatus status() {
        return resultCalculate();
    }
    private SprayCoordinatorStatus resultCalculate() {
        for (Map.Entry<String, SprayProcessStepExecutor> stepExecutor : this.cachedExecutorMap.entrySet()) {
            // [STOP, FAILED, DONE, ERROR, PAUSED, RUNNING]
            SprayType executorStatus = stepExecutor.getValue().getStepResult().getStatus();
            if (SprayStepStatus.DONE.equals(executorStatus)) {
                // find next if cur is done
                continue;
            }
            if (SprayStepStatus.FAILED.equals(executorStatus) ||
                    SprayStepStatus.ERROR.equals(executorStatus)) {
                return SprayCoordinatorStatus.FAILED;
            }
            if (SprayStepStatus.STOP.equals(executorStatus)) {
                return SprayCoordinatorStatus.STOP;
            }
            // other means running
            return SprayCoordinatorStatus.RUNNING;
        }
        return SprayCoordinatorStatus.SUCCESS;
    }


    @Override
    public void run() {
        if (this.defaultDataList.isEmpty()) {
            this._dispatchData(this.getMeta().getStartNodes(), null,
                    this.defaultVariables, null, null, false);
        } else {
            for (int i = 0; i < this.defaultDataList.size() - 1; i++) {
                this._dispatchData(this.getMeta().getStartNodes(), null,
                        this.defaultVariables, null, this.defaultDataList.get(i), true);
            }
            this._dispatchData(this.getMeta().getStartNodes(), null,
                    this.defaultVariables, null, this.defaultDataList.get(this.defaultDataList.size() - 1), false);
        }
    }

//    @Override
//    public void publishData(String variablesIdentityDataKey, SprayNextStepFilter stepFilter,
//                            SprayProcessStepExecutor fromExecutor, SprayData data, boolean still) {
//        this._dispatchData(fromExecutor.getMeta().nextNodes(), stepFilter, this.executorVariablesNamespace.get(variablesIdentityDataKey), fromExecutor, data, still);
//    }


    protected void setDispatchResult(SprayVariableContainer variables, SprayProcessStepExecutor fromExecutor,
                                     SprayData data, boolean still, SprayProcessStepMeta nextMeta,
                                     SprayDataDispatchResultStatus dataDispatchStatus) {
        this.dispatchResultHandler.setDispatchResult(
                this.getMeta(), variables, fromExecutor, data, still,
                nextMeta, this.computeDataKey(variables, fromExecutor, data, still, nextMeta),
                dataDispatchStatus);
    }

    protected String computeDataKey(SprayVariableContainer variables, SprayProcessStepExecutor fromExecutor,
                                    SprayData data, boolean still, SprayProcessStepMeta nextMeta) {
        String dataKey = this.dispatchResultHandler.computeDataKey(this.getMeta(), variables, fromExecutor, data, still, nextMeta);
        this.inputDataKeys.computeIfAbsent(fromExecutor.getExecutorNameKey(),
                k-> new LinkedHashSet<>()).add(dataKey);
        this.outputDataKeys.computeIfAbsent(nextMeta.getExecutorNameKey(this.getMeta()),
                k-> new LinkedHashSet<>()).add(dataKey);
        return dataKey;
    }

    private boolean validateBeforeCreate(SprayVariableContainer lastVariables, SprayProcessStepExecutor fromExecutor, SprayNextStepFilter stepFilter, SprayData data, boolean still, SprayProcessStepMeta nodeMeta) {
        boolean create = true;
        if (!SprayStepActiveType.ACTIVE.equals(nodeMeta.stepActiveType())) {
            if (SprayStepActiveType.IGNORE.equals(nodeMeta.stepActiveType())) {
                this._dispatchData(nodeMeta.nextNodes(), stepFilter, this.defaultVariables, fromExecutor, data, still);
            }
            this.setDispatchResult(lastVariables, fromExecutor, data, still, nodeMeta, SprayDataDispatchResultStatus.SKIPPED);
            create = false;
        }
        if (stepFilter != null) {
            if (!stepFilter.executableForNext(fromExecutor, data, still, nodeMeta)) {
                this.setDispatchResult(lastVariables, fromExecutor, data, still, nodeMeta, SprayDataDispatchResultStatus.FILTERED);
                create = false;
            }
        }
        if (! nodeMeta.getExecuteConditionFilter().isEmpty()) {
            for (SprayStepExecuteConditionFilter filter : nodeMeta.getExecuteConditionFilter()) {
                if (! filter.executableForMe(fromExecutor, data, still, nodeMeta)) {
                    create = false;
                    this.setDispatchResult(lastVariables, fromExecutor, data, still, nodeMeta, SprayDataDispatchResultStatus.ABANDONED);
                    break;
                }
            }
        }
        return create;
    }

    private void _dispatchData(List<SprayProcessStepMeta> nodes, SprayNextStepFilter stepFilter, SprayVariableContainer lastVariables, SprayProcessStepExecutor fromExecutor, SprayData data, boolean still) {
        if (lastVariables == null) {
            lastVariables = defaultVariables;
        }
        if (nodes == null || nodes.isEmpty()) {
            this.setDispatchResult(lastVariables, fromExecutor, data, still, null, SprayDataDispatchResultStatus.ABANDONED);
            return;
        }
        for (SprayProcessStepMeta nodeMeta : nodes) {
            if (! validCoordinatorStatus()) {
                continue;
            }
            if (! validateBeforeCreate(lastVariables, fromExecutor, stepFilter, data, still, nodeMeta)) {
                continue;
            }
            // run it
            this.send(nodeMeta.getExecutorNameKey(this.getMeta()), lastVariables,
                    fromExecutor.getExecutorNameKey(), data, still);
        }
    }


    private boolean validCoordinatorStatus() {
        return SprayCoordinatorStatus.RUNNING.equals(this.status());
    }


    @Override
    public void send(String curExecutorNameKey, SprayVariableContainer lastVariables,
                     String fromExecutorNameKey, SprayData data, boolean still) {
        SprayProcessStepExecutor fromExecutor = this.getStepExecutor(fromExecutorNameKey);
        SprayProcessStepExecutor curExecutor = this.getStepExecutor(curExecutorNameKey);
        try {
            int copyMode = curExecutor.varCopyMode();
            if (copyMode != 0) {
                curExecutor.receive(new SprayReceiveDataEvent(lastVariables, fromExecutorNameKey, data, still));
            } else {
                curExecutor.receive(new SprayReceiveDataEvent(
                        executorVariablesNamespace.computeIfAbsent(
                                lastVariables.nextKey(fromExecutor, curExecutor), key -> copyMode == 1 ?
                                        SprayExecutorVariableContainer.easyCopy(fromExecutor, lastVariables, curExecutor) :
                                        SprayExecutorVariableContainer.deepCopy(fromExecutor, lastVariables, curExecutor)),
                        fromExecutorNameKey, data, still));
            }
        } catch (Exception e) {
            this.setDispatchResult(lastVariables, fromExecutor, data, still, curExecutor.getMeta(), SprayDataDispatchResultStatus.SUCCESS);
        }
    }



    @Override
    public SprayVariableContainer getVariablesContainer(String identityDataKey) {
        return this.executorVariablesNamespace.get(identityDataKey);
    }

    @Override
    public final SprayProcessStepExecutor getStepExecutor(String executorNameKey) {
        return executorNameKey == null ? null : this.cachedExecutorMap.get(executorNameKey);
    }

    @Override
    public Set<String> getInputDataKeys(String executorNameKey) {
        return inputDataKeys.computeIfAbsent(executorNameKey, k-> new LinkedHashSet<>());
    }

    @Override
    public Set<String> getOutputDataKeys(String executorNameKey) {
        return outputDataKeys.computeIfAbsent(executorNameKey, k-> new LinkedHashSet<>());
    }

    @Override
    public List<SprayDataDispatchResultStatus> getDispatchResults(String dataKey) {
        return this.dispatchResultHandler.getDispatchResult(this.getMeta(), dataKey);
    }
    @Override
    public void closeInRuntime() {
        this.dispatchResultHandler.whenCoordinatorShutdown(this.getMeta());
        if (this.getCreatorThreadClassLoader() instanceof SprayClassLoader scl) {
            scl.closeInRuntime();
        }
        for (Map.Entry<String, SprayProcessStepExecutor> executorEntry : this.cachedExecutorMap.entrySet()) {
            try {
                executorEntry.getValue().close();
            } catch (Exception e) {
                // TODO handle exception with its handler
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void receive(SprayEvent event) {
        handleWith(event);
    }

    private void handleWith(SprayEvent event) {
        List<Method> handleMethods = this.coordinateEventMethods.get(event.getEventName());
        if (CollUtil.isEmpty(handleMethods)) {
            handleUnknownEvent(new SprayUnknownEvent(event));
        }
    }
    protected void handleUnknownEvent(SprayUnknownEvent unknownEvent) {
        // TODO get unknown event handlers
        this.getListeners().stream().filter(listener -> listener.support(unknownEvent))
                .forEach(listener -> listener.handle(unknownEvent, this));
    }

}
