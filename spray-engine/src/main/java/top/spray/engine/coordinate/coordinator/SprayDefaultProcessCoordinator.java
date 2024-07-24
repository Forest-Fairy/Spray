package top.spray.engine.coordinate.coordinator;

import top.spray.core.engine.exception.SprayNotSupportError;
import top.spray.core.engine.execute.SprayListenable;
import top.spray.core.engine.execute.SprayStepActiveType;
import top.spray.core.engine.props.SprayData;
import top.spray.core.engine.status.SprayStatusType;
import top.spray.core.engine.status.impl.SprayDataDispatchResultStatus;
import top.spray.core.engine.status.impl.SprayStepStatus;
import top.spray.core.engine.status.impl.SprayCoordinateStatus;
import top.spray.engine.coordinate.handler.result.SprayDataDispatchResultHandler;
import top.spray.engine.exception.SprayExecutorGenerateError;
import top.spray.engine.factory.SprayExecutorFactory;
import top.spray.engine.prop.SprayVariableContainer;
import top.spray.engine.step.condition.SprayStepExecuteConditionFilter;
import top.spray.engine.step.executor.SprayExecutorListener;
import top.spray.engine.coordinate.meta.SprayProcessCoordinatorMeta;
import top.spray.engine.step.executor.SprayProcessStepExecutor;
import top.spray.engine.step.condition.SprayNextStepFilter;
import top.spray.engine.step.meta.SprayProcessStepMeta;

import java.util.*;
import java.util.concurrent.*;

public class SprayDefaultProcessCoordinator implements
        SprayProcessCoordinator,
        SprayListenable<SprayExecutorListener> {
    private final SprayProcessCoordinatorMeta coordinatorMeta;
    private final List<SprayExecutorListener> listeners;
    private final SprayVariableContainer defaultVariables;
    private final List<SprayData> defaultDataList;
    private final SprayDataDispatchResultHandler dispatchResultHandler;
    private final ClassLoader creatorThreadClassLoader;
    private final Map<String, SprayProcessStepExecutor> cachedExecutorMap;
    /** a namespace for executor's process data */
    private final Map<String, SprayVariableContainer> executorVariablesNamespace;
    private final Map<String, Set<String>> inputDataKeys;
    private final Map<String, Set<String>> outputDataKeys;
    private boolean executeOnlyOnce = true;

    public SprayDefaultProcessCoordinator(SprayProcessCoordinatorMeta coordinatorMeta) {
        this.cachedExecutorMap = new ConcurrentHashMap<>();
        this.executorVariablesNamespace = new ConcurrentHashMap<>();
        this.coordinatorMeta = coordinatorMeta;
        this.listeners = new ArrayList<>();
        this.defaultVariables = SprayVariableContainer.create(this);
        this.defaultDataList = new ArrayList<>();
        this.creatorThreadClassLoader = Thread.currentThread().getContextClassLoader();
        this.dispatchResultHandler = SprayDataDispatchResultHandler.get(coordinatorMeta);
        this.inputDataKeys = new ConcurrentHashMap<>();
        this.outputDataKeys = new ConcurrentHashMap<>();
        init();
    }

    private void init() {
        // don't let the coordinator run twice so that we could clear the default data list when we run
//        while (this.counter.sum() > 0) {
//            Thread.yield();
//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//                throw new RuntimeException(new Exception(
//                        "failed to init the coordinator for current thread was interrupted", e));
//            }
//        }
        if (!executeOnlyOnce) {
            throw new SprayNotSupportError(this.coordinatorMeta,
                    new IllegalAccessError("the coordinator can only be run once"));
        }
        executeOnlyOnce = false;
        initVar();
    }

    private void initVar() {
        if (this.coordinatorMeta.getDefaultDataList() != null) {
            this.defaultDataList.addAll(this.coordinatorMeta.getDefaultDataList());
        }
    }


    @Override
    public final SprayDefaultProcessCoordinator addListener(SprayExecutorListener... listeners) {
        this.listeners.addAll(Arrays.asList(listeners));
        return this;
    }

    @Override
    public final SprayProcessCoordinatorMeta getMeta() {
        return coordinatorMeta;
    }


    @Override
    public final List<SprayExecutorListener> getListeners() {
        return this.listeners;
    }

    @Override
    public final ClassLoader getCreatorThreadClassLoader() {
        return this.creatorThreadClassLoader;
    }

    @Override
    public final SprayProcessStepExecutor getStepExecutor(SprayProcessStepMeta executorMeta) {
        String executorNameKey = executorMeta.getExecutorNameKey(this.getMeta());
        SprayProcessStepExecutor executor = this.cachedExecutorMap.get(executorNameKey);
        if (executor == null) {
            synchronized (this.cachedExecutorMap) {
                executor = this.cachedExecutorMap.get(executorNameKey);
                if (executor == null) {
                    try {
                        // all executors' classloader should be created with the same classloader
                        // which create the coordinator
                        executor = SprayExecutorFactory.create(this, executorMeta, true);
                    } catch (Throwable e) {
                        throw new SprayExecutorGenerateError(this.getMeta(), executorMeta, e);
                    }
                    this.cachedExecutorMap.put(executorNameKey, executor);
                }
            }
        }
        return executor;
    }

    @Override
    public final int createExecutorCount() {
        return this.cachedExecutorMap.size();
    }

    @Override
    public final SprayCoordinateStatus status() {
        return calculateTheResult();
    }
    private SprayCoordinateStatus calculateTheResult() {
        for (Map.Entry<String, SprayProcessStepExecutor> stepExecutor : this.cachedExecutorMap.entrySet()) {
            // [STOP, FAILED, DONE, ERROR, PAUSED, RUNNING]
            SprayStatusType executorStatus = stepExecutor.getValue().getStepResult().getStatus();
            if (SprayStepStatus.DONE.equals(executorStatus)) {
                // find next if cur is done
                continue;
            }
            if (SprayStepStatus.FAILED.equals(executorStatus) ||
                    SprayStepStatus.ERROR.equals(executorStatus)) {
                return SprayCoordinateStatus.FAILED;
            }
            if (SprayStepStatus.STOP.equals(executorStatus)) {
                return SprayCoordinateStatus.STOP;
            }
            // other means running
            return SprayCoordinateStatus.RUNNING;
        }
        return SprayCoordinateStatus.SUCCESS;
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

    @Override
    public void dispatch(SprayVariableContainer lastVariables, SprayProcessStepExecutor fromExecutor,
                         SprayNextStepFilter stepFilter, SprayData data, boolean still) {
        this._dispatchData(fromExecutor.getMeta().nextNodes(), stepFilter, lastVariables, fromExecutor, data, still);
    }


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

    @Override
    public Map<String, SprayProcessStepExecutor> getCachedExecutorMap() {
        return cachedExecutorMap;
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
            // get or create executor
            SprayProcessStepExecutor nextExecutor = this.getStepExecutor(nodeMeta);
            if (needWait(nextExecutor, lastVariables, fromExecutor, data, still)) {
                // the executor need to wait
                return;
            }
            // run it
            this.executeNext(nextExecutor,
                    lastVariables, fromExecutor, data, still);
        }
    }

    private boolean needWait(SprayProcessStepExecutor nextExecutor, SprayVariableContainer lastVariables, SprayProcessStepExecutor fromExecutor, SprayData data, boolean still) {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        Thread.currentThread().setContextClassLoader(nextExecutor.getClassLoader());
        boolean needWait = nextExecutor.needWait(lastVariables, fromExecutor, data, still);
        Thread.currentThread().setContextClassLoader(loader);
        return needWait;
    }

    private boolean validCoordinatorStatus() {
        return SprayCoordinateStatus.RUNNING.equals(this.status());
    }


    @Override
    public void executeNext(SprayProcessStepExecutor curExecutor,
                            SprayVariableContainer lastVariables,
                            SprayProcessStepExecutor fromExecutor,
                            SprayData data, boolean still) {
        try {
            int copyMode = curExecutor.varCopyMode();
            if (copyMode == 0) {
                curExecutor.execute(lastVariables, fromExecutor, data, still);
            } else {
                curExecutor.execute(
                        executorVariablesNamespace.computeIfAbsent(
                                lastVariables.nextKey(fromExecutor, curExecutor), key -> copyMode == 1 ?
                                        SprayVariableContainer.easyCopy(fromExecutor, lastVariables, curExecutor) :
                                        SprayVariableContainer.deepCopy(fromExecutor, lastVariables, curExecutor)),
                        fromExecutor, data, still);
            }
        } catch (Exception e) {
            this.setDispatchResult(lastVariables, fromExecutor, data, still, curExecutor.getMeta(), SprayDataDispatchResultStatus.SUCCESS);
        }
    }

    @Override
    public void close() throws Exception {
        this.dispatchResultHandler.whenCoordinatorShutdown(this.getMeta());
        for (Map.Entry<String, SprayProcessStepExecutor> executorEntry : this.cachedExecutorMap.entrySet()) {
            try {
                executorEntry.getValue().close();
            } catch (Exception e) {
                // TODO handle exception with its handler
                throw e;
            }
        }
    }
}
