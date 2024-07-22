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
import top.spray.engine.factory.SprayExecutorFactory;
import top.spray.engine.prop.SprayVariableContainer;
import top.spray.engine.step.condition.SprayStepExecuteConditionFilter;
import top.spray.engine.step.executor.SprayExecutorListener;
import top.spray.engine.coordinate.meta.SprayProcessCoordinatorMeta;
import top.spray.engine.step.executor.SprayProcessStepExecutor;
import top.spray.engine.step.condition.SprayNextStepFilter;
import top.spray.engine.step.meta.SprayProcessStepMeta;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Stream;

public class SprayDefaultProcessCoordinator implements
        SprayProcessCoordinator,
        SprayListenable<SprayExecutorListener> {
    private final Map<String, SprayProcessStepExecutor> cachedExecutorMap;
    /** a namespace for executor's process data */
    private final Map<String, SprayVariableContainer> executorVariablesNamespace;
    private final SprayProcessCoordinatorMeta coordinatorMeta;
    private final List<SprayExecutorListener> listeners;
    private final SprayVariableContainer defaultVariables;
    private final List<SprayData> defaultDataList;
    private final ClassLoader creatorThreadClassLoader;
    private final SprayDataDispatchResultHandler dispatchResultHandler;
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
    public SprayDefaultProcessCoordinator addListener(SprayExecutorListener... listeners) {
        this.listeners.addAll(Arrays.asList(listeners));
        return this;
    }

    @Override
    public SprayProcessCoordinatorMeta getMeta() {
        return coordinatorMeta;
    }


    @Override
    public List<SprayExecutorListener> getListeners() {
        return this.listeners;
    }

    @Override
    public ClassLoader getCreatorThreadClassLoader() {
        return this.creatorThreadClassLoader;
    }

    @Override
    public SprayProcessStepExecutor getStepExecutor(SprayProcessStepMeta executorMeta) {
        String executorNameKey = executorMeta.getExecutorNameKey(this);
        SprayProcessStepExecutor executor = this.cachedExecutorMap.get(executorNameKey);
        if (executor == null) {
            synchronized (this.cachedExecutorMap) {
                executor = this.cachedExecutorMap.get(executorNameKey);
                if (executor == null) {
                    executor = SprayExecutorFactory.create(this, executorMeta);
                    this.cachedExecutorMap.put(executorNameKey, executor);
                }
            }
        }
        return executor;
    }

    @Override
    public int createExecutorCount() {
        return this.cachedExecutorMap.size();
    }

    @Override
    public SprayCoordinateStatus status() {
        return calculateTheResult();
    }
    private SprayCoordinateStatus calculateTheResult() {
        for (Map.Entry<String, SprayProcessStepExecutor> stepExecutor : this.cachedExecutorMap.entrySet()) {
            // [STOP, FAILED, DONE, ERROR, PAUSED, RUNNING]
            SprayStatusType executorStatus = stepExecutor.getValue().getStepResult().getStatus();
            if (SprayStepStatus.FAILED.equals(executorStatus) || SprayStepStatus.ERROR.equals(executorStatus)) {
                return SprayCoordinateStatus.FAILED;
            }
            if (SprayStepStatus.STOP.equals(executorStatus)) {
                return SprayCoordinateStatus.STOP;
            }
            if (!SprayStepStatus.DONE.equals(executorStatus)) {
                return SprayCoordinateStatus.RUNNING;
            }
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
                this, variables, fromExecutor, data, still,
                nextMeta, this.computeDataKey(variables, fromExecutor, data, still, nextMeta),
                dataDispatchStatus);
    }


    public List<SprayDataDispatchResultStatus> getDispatchResults(String dataKey) {
        return this.dispatchResultHandler.getDispatchResult(this, dataKey);
    }

    protected String computeDataKey(SprayVariableContainer variables, SprayProcessStepExecutor fromExecutor,
                                    SprayData data, boolean still, SprayProcessStepMeta nextMeta) {
        String dataKey = this.dispatchResultHandler.computeDataKey(this, variables, fromExecutor, data, still, nextMeta);
        this.inputDataKeys.computeIfAbsent(fromExecutor.getExecutorNameKey(),
                k-> new LinkedHashSet<>()).add(dataKey);
        this.outputDataKeys.computeIfAbsent(nextMeta.getExecutorNameKey(this),
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
    public void executeNext(SprayProcessStepExecutor nextStepExecutor,
                            SprayVariableContainer lastVariables,
                            SprayProcessStepExecutor fromExecutor,
                            SprayData data, boolean still) {
        try {
            int copyMode = nextStepExecutor.varCopyMode();
            if (copyMode == 0) {
                nextStepExecutor.execute(lastVariables, fromExecutor, data, still);
            } else {
                nextStepExecutor.execute(
                        executorVariablesNamespace.computeIfAbsent(
                                lastVariables.nextKey(fromExecutor, nextStepExecutor), key -> copyMode == 1 ?
                                        SprayVariableContainer.easyCopy(fromExecutor, lastVariables, nextStepExecutor) :
                                        SprayVariableContainer.deepCopy(fromExecutor, lastVariables, nextStepExecutor)),
                        fromExecutor, data, still);
            }
        } catch (Exception e) {
            this.setDispatchResult(lastVariables, fromExecutor, data, still, nextStepExecutor.getMeta(), SprayDataDispatchResultStatus.SUCCESS);
        }
    }

//    protected void afterExecute(SprayProcessStepExecutor nextStepExecutor,
//                                SprayProcessStepExecutor fromExecutor,
//                                SprayData data, boolean still) {
//        if (SprayStepStatus.DONE.equals(nextStepExecutor.getStepResult().getStatus())) {
//            this.endUpWithExecutor(nextStepExecutor);
//        }
//    }

//    private void endUpWithExecutor(SprayProcessStepExecutor executor) {
//        if (executor instanceof SprayTransactionSupportExecutor transactionSupportExecutor) {
//            if (SprayStepStatus.DONE.equals(executor.getStepResult().getStatus())) {
//                transactionSupportExecutor.commit();
//            } else if (SprayStepStatus.FAILED.equals(executor.getStepResult().getStatus()) ||
//                    SprayStepStatus.STOP.equals(executor.getStepResult().getStatus())) {
//                transactionSupportExecutor.rollback();
//            }
//        }
//        if (executor instanceof SprayCloseableExecutor closeableExecutor) {
//            try {
//                closeableExecutor.close();
//            } catch (Throwable closeException) {
//                closeableExecutor.closeFailed(closeException);
//            }
//        }
//    }

//    private SprayData getExecutorProcessData(SprayProcessStepMeta fromExecutorMeta, SprayProcessStepMeta curExecutorMeta) {
//        int copyMode = curExecutorMeta.varCopyMode();
//        String curExecutorExecutorNameKey = this.getExecutorNameKey(curExecutorMeta);
//        SprayData executorProcessDataForUsing;
//        // firstly get the base process data for cur executor
//        if (fromExecutorMeta == null) {
//            // first executor in the executor line   ○ -> ○ -> ○
//            //                                       ↑
//            executorProcessDataForUsing = this.processData;
//            if (copyMode == 0) {
//                // not copy that it needs no namespace
//                return executorProcessDataForUsing;
//            }
//            // try to get from namespace
//            SprayData curExecutorProcessData = executorVariablesNamespace.get(curExecutorExecutorNameKey);
//            if (curExecutorProcessData != null) {
//                return curExecutorProcessData;
//            }
//        } else {
//            String lastExecutorExecutorNameKey = this.getExecutorNameKey(fromExecutorMeta);
//            if (copyMode == 0) {
//                executorProcessDataForUsing = executorVariablesNamespace.get(lastExecutorExecutorNameKey);
//                if (executorProcessDataForUsing == null) {
//                    // none copy between last and current that it needs no namespace
//                    return this.processData;
//                }
////                deal with it in a code block
////                else {
////                    // setting a namespace for cur executor
////                    executorProcessDataNamespace.put(curExecutorExecutorNameKey, executorProcessDataForUsing);
////                }
//                // try to get from namespace
//                SprayData curExecutorProcessData = executorVariablesNamespace.get(curExecutorExecutorNameKey);
//                if (curExecutorProcessData != null) {
//                    return curExecutorProcessData;
//                }
//            } else {
//                // copy mode
//
//                // try to get from namespace
//                SprayData curExecutorProcessData = executorVariablesNamespace.get(curExecutorExecutorNameKey);
//                if (curExecutorProcessData != null) {
//                    return curExecutorProcessData;
//                }
//
//                executorProcessDataForUsing = executorVariablesNamespace.get(lastExecutorExecutorNameKey);
//                if (executorProcessDataForUsing == null) {
//                    // no copy mode executor in all the lasts.
//                    executorProcessDataForUsing = this.processData;
//                }
//            }
//        }
//        if (copyMode != 0) {
//            // copy mode
//            if (copyMode == 1) {
//                // easy copy
//                executorProcessDataForUsing = new SprayData(executorProcessDataForUsing);
//            } else {
//                // deep copy
//                executorProcessDataForUsing = SprayData.deepCopy(executorProcessDataForUsing);
//            }
//        }
//        // setting a namespace for cur executor
//        executorVariablesNamespace.put(curExecutorExecutorNameKey, executorProcessDataForUsing);
//        return executorProcessDataForUsing;
//    }

    @Override
    public void close() throws IOException {

    }
}
