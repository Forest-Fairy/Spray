package top.spray.engine.coordinate.coordinator;

import top.spray.core.engine.exception.SprayNotSupportError;
import top.spray.core.engine.execute.SprayListenable;
import top.spray.core.engine.execute.SprayStepActiveType;
import top.spray.core.engine.props.SprayData;
import top.spray.core.engine.status.SprayStatusType;
import top.spray.core.engine.status.impl.SprayDataDispatchResultStatus;
import top.spray.core.thread.SprayPoolExecutor;
import top.spray.core.engine.status.impl.SprayStepStatus;
import top.spray.core.engine.status.impl.SprayCoordinateStatus;
import top.spray.engine.prop.SprayVariableContainer;
import top.spray.engine.step.condition.SprayStepExecuteConditionFilter;
import top.spray.engine.step.executor.SprayExecutorListener;
import top.spray.engine.coordinate.meta.SprayProcessCoordinatorMeta;
import top.spray.engine.step.executor.SprayProcessStepExecutor;
import top.spray.engine.factory.SprayExecutorFactory;
import top.spray.engine.step.condition.SprayNextStepFilter;
import top.spray.engine.step.meta.SprayProcessStepMeta;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;

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
    private final long creatorThreadId;

    private SprayPoolExecutor executor;

    private boolean executeOnlyOnce = true;

    public SprayDefaultProcessCoordinator(SprayProcessCoordinatorMeta coordinatorMeta) {
        this.cachedExecutorMap = new ConcurrentHashMap<>();
        this.executorVariablesNamespace = new ConcurrentHashMap<>();
        this.coordinatorMeta = coordinatorMeta;
        this.listeners = new ArrayList<>();
        this.defaultVariables = SprayVariableContainer.create(this);
        this.defaultDataList = new ArrayList<>();
        this.creatorThreadId = Thread.currentThread().getId();
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
        initThreadExecutor();
    }

    private void initThreadExecutor() {
        boolean asyncSupport = this.coordinatorMeta.asyncSupport();
        if (asyncSupport) {
            // TODO performance the thread executor
            this.executor = new SprayPoolExecutor(
                    Math.max(0, this.coordinatorMeta.minThreadCount()),
                    Integer.MAX_VALUE,
                    3, TimeUnit.SECONDS,
                    new LinkedBlockingQueue<>(Math.max(0, this.coordinatorMeta.minThreadCount()))
            );
        } else {
            this.executor = null;
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
    public SprayPoolExecutor getSprayPoolExecutor() {
        return this.executor;
    }

    @Override
    public void registerExecutor(String executorId, SprayProcessStepExecutor executor) {
        this.cachedExecutorMap.put(executorId, executor);
    }

    @Override
    public SprayProcessStepExecutor getStepExecutor(String executorId) {
        return this.cachedExecutorMap.get(executorId);
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
        boolean isAsyncRun = this.creatorThreadId != Thread.currentThread().getId();
        if (this.defaultDataList.isEmpty()) {
            this._dispatchData(this.getMeta().getStartNodes(), null, isAsyncRun,
                    this.defaultVariables, null, null, false);
        } else {
            for (int i = 0; i < this.defaultDataList.size() - 1; i++) {
                this._dispatchData(this.getMeta().getStartNodes(), null, isAsyncRun,
                        this.defaultVariables, null, this.defaultDataList.get(i), true);
            }
            this._dispatchData(this.getMeta().getStartNodes(), null, isAsyncRun,
                    this.defaultVariables, null, this.defaultDataList.get(this.defaultDataList.size() - 1), false);
        }
    }

    private SprayPoolExecutor getSprayPoolExecutor(SprayProcessStepExecutor fromExecutor) {
        // TODO need a executor pool designed for the executor
        // if from executor contains info for its own executor then make one for it
        // else use the process executor
        return this.getSprayPoolExecutor();
    }

    @Override
    public void dispatch(SprayVariableContainer lastVariables, SprayProcessStepExecutor fromExecutor, SprayNextStepFilter stepFilter,
                         SprayData data, boolean still, boolean dispatchAsync) {
        if (dispatchAsync) {
            SprayPoolExecutor poolExecutor = this.getSprayPoolExecutor(fromExecutor);
            poolExecutor.execute(() -> this._dispatchData(fromExecutor.getMeta().nextNodes(), stepFilter, true, lastVariables, fromExecutor, data, still));
        } else {
            this._dispatchData(fromExecutor.getMeta().nextNodes(), stepFilter, false, lastVariables, fromExecutor, data, still);
        }
    }

    protected void setDispatchResult(SprayVariableContainer variables, SprayProcessStepExecutor fromExecutor, SprayData data, boolean still, boolean async,
                                     SprayProcessStepMeta nextMeta, SprayDataDispatchResultStatus dataDispatchStatus) {
        // TODO get data status save strategies
    }


    protected SprayDataDispatchResultStatus getDispatchResult(String dataKey) {
        // TODO get data status save strategies
    }

    private boolean validateBeforeCreate(SprayVariableContainer lastVariables, SprayProcessStepExecutor fromExecutor, SprayNextStepFilter stepFilter, SprayData data, boolean still, boolean dispatchAsync, SprayProcessStepMeta nodeMeta) {
        boolean create = true;
        if (!SprayStepActiveType.ACTIVE.equals(nodeMeta.stepActiveType())) {
            if (SprayStepActiveType.IGNORE.equals(nodeMeta.stepActiveType())) {
                this._dispatchData(nodeMeta.nextNodes(), stepFilter, dispatchAsync, this.defaultVariables, fromExecutor, data, still);
            }
            this.setDispatchResult(lastVariables, fromExecutor, data, still, dispatchAsync, nodeMeta, SprayDataDispatchResultStatus.SKIPPED);
            create = false;
        }
        if (stepFilter != null) {
            if (!stepFilter.canBeExecute(fromExecutor, data, still, nodeMeta)) {
                this.setDispatchResult(lastVariables, fromExecutor, data, still, dispatchAsync, nodeMeta, SprayDataDispatchResultStatus.FILTERED);
                create = false;
            }
        }
        if (! nodeMeta.getExecuteConditionFilter().isEmpty()) {
            for (SprayStepExecuteConditionFilter filter : nodeMeta.getExecuteConditionFilter()) {
                if (!filter.filter(fromExecutor, data, still, nodeMeta)) {
                    create = false;
                    this.setDispatchResult(lastVariables, fromExecutor, data, still, dispatchAsync, nodeMeta, SprayDataDispatchResultStatus.ABANDONED);
                    break;
                }
            }
        }
        return create;
    }

    private void _dispatchData(List<SprayProcessStepMeta> nodes, SprayNextStepFilter stepFilter, boolean dispatchAsync, SprayVariableContainer lastVariables, SprayProcessStepExecutor fromExecutor, SprayData data, boolean still) {
        if (nodes == null || nodes.isEmpty()) {
            this.setDispatchResult(lastVariables, fromExecutor, data, still, dispatchAsync, null, SprayDataDispatchResultStatus.ABANDONED);
            return;
        }
        List<CompletableFuture<Void>> futureResults = new ArrayList<>();
        for (SprayProcessStepMeta nodeMeta : nodes) {
            if (! validCoordinatorStatus()) {

            }
            if (! validateBeforeCreate(lastVariables, fromExecutor, stepFilter, data, still, dispatchAsync, nodeMeta)) {
                continue;
            }
            SprayProcessStepExecutor nextExecutor = SprayExecutorFactory.create(this, nodeMeta);
            if (nextExecutor.needWait(lastVariables, fromExecutor, data, still)) {
                // the executor need to wait
                continue;
            }
            // run it
            if (nodeMeta.isAsync() && this.getSprayPoolExecutor() != null) {
                {
                    // TODO check and ensure that the last data run after all before sending data...
                }
                futureResults.add(CompletableFuture.runAsync(
                        // this will create a new thread to run the step fully
                        () -> this.executeNext(nextExecutor, lastVariables, fromExecutor, data, still),
                        this.getSprayPoolExecutor()));
            } else {
                this.executeNext(nextExecutor, lastVariables, fromExecutor, data, still);
            }
        }
        futureResults.forEach(CompletableFuture::join);
    }

    private boolean validCoordinatorStatus() {
        SprayCoordinateStatus status = this.status();
        if (status)
        return false;
    }


    @Override
    public void executeNext(SprayProcessStepExecutor nextStepExecutor, SprayVariableContainer lastVariables,
                            SprayProcessStepExecutor fromExecutor,
                            SprayData data, boolean still) {
        int copyMode = nextStepExecutor.getMeta().varCopyMode();
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
    }

    private SprayVariableContainer computeVariableForExecutor(SprayVariableContainer variables, SprayProcessStepExecutor nextStepExecutor) {
        executorVariablesNamespace.computeIfAbsent(this.getExecutorNameKey(nextStepExecutor))
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

    private SprayData getExecutorProcessData(SprayProcessStepMeta fromExecutorMeta, SprayProcessStepMeta curExecutorMeta) {
        int copyMode = curExecutorMeta.varCopyMode();
        String curExecutorExecutorNameKey = this.getExecutorNameKey(curExecutorMeta);
        SprayData executorProcessDataForUsing;
        // firstly get the base process data for cur executor
        if (fromExecutorMeta == null) {
            // first executor in the executor line   ○ -> ○ -> ○
            //                                       ↑
            executorProcessDataForUsing = this.processData;
            if (copyMode == 0) {
                // not copy that it needs no namespace
                return executorProcessDataForUsing;
            }
            // try to get from namespace
            SprayData curExecutorProcessData = executorVariablesNamespace.get(curExecutorExecutorNameKey);
            if (curExecutorProcessData != null) {
                return curExecutorProcessData;
            }
        } else {
            String lastExecutorExecutorNameKey = this.getExecutorNameKey(fromExecutorMeta);
            if (copyMode == 0) {
                executorProcessDataForUsing = executorVariablesNamespace.get(lastExecutorExecutorNameKey);
                if (executorProcessDataForUsing == null) {
                    // none copy between last and current that it needs no namespace
                    return this.processData;
                }
//                deal with it in a code block
//                else {
//                    // setting a namespace for cur executor
//                    executorProcessDataNamespace.put(curExecutorExecutorNameKey, executorProcessDataForUsing);
//                }
                // try to get from namespace
                SprayData curExecutorProcessData = executorVariablesNamespace.get(curExecutorExecutorNameKey);
                if (curExecutorProcessData != null) {
                    return curExecutorProcessData;
                }
            } else {
                // copy mode

                // try to get from namespace
                SprayData curExecutorProcessData = executorVariablesNamespace.get(curExecutorExecutorNameKey);
                if (curExecutorProcessData != null) {
                    return curExecutorProcessData;
                }

                executorProcessDataForUsing = executorVariablesNamespace.get(lastExecutorExecutorNameKey);
                if (executorProcessDataForUsing == null) {
                    // no copy mode executor in all the lasts.
                    executorProcessDataForUsing = this.processData;
                }
            }
        }
        if (copyMode != 0) {
            // copy mode
            if (copyMode == 1) {
                // easy copy
                executorProcessDataForUsing = new SprayData(executorProcessDataForUsing);
            } else {
                // deep copy
                executorProcessDataForUsing = SprayData.deepCopy(executorProcessDataForUsing);
            }
        }
        // setting a namespace for cur executor
        executorVariablesNamespace.put(curExecutorExecutorNameKey, executorProcessDataForUsing);
        return executorProcessDataForUsing;
    }

    @Override
    public void close() throws IOException {

    }
}
