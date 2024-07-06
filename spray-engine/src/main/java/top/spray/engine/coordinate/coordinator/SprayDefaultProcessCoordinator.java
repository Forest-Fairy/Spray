package top.spray.engine.coordinate.coordinator;

import top.spray.core.engine.exception.SprayNotSupportError;
import top.spray.core.engine.execute.SprayListenable;
import top.spray.core.engine.execute.SprayStepActiveType;
import top.spray.core.engine.props.SprayData;
import top.spray.core.engine.result.SprayStatusType;
import top.spray.core.engine.result.impl.SprayDataDispatchResultStatus;
import top.spray.core.thread.SprayPoolExecutor;
import top.spray.core.engine.result.impl.SprayStepStatus;
import top.spray.core.engine.result.impl.SprayCoordinateStatus;
import top.spray.engine.step.condition.SprayStepExecuteConditionFilter;
import top.spray.engine.step.executor.SprayExecutorListener;
import top.spray.engine.coordinate.meta.SprayProcessCoordinatorMeta;
import top.spray.engine.step.executor.SprayProcessStepExecutor;
import top.spray.engine.step.executor.closeable.SprayCloseableExecutor;
import top.spray.engine.factory.SprayExecutorFactory;
import top.spray.engine.step.condition.SprayNextStepFilter;
import top.spray.engine.step.executor.transaction.SprayTransactionSupportExecutor;
import top.spray.engine.step.meta.SprayProcessStepMeta;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.LongAdder;

public class SprayDefaultProcessCoordinator implements
        SprayProcessCoordinator,
        SprayListenable<SprayExecutorListener> {
    private final Map<String, SprayProcessStepExecutor> cachedExecutorMap;
    /** a namespace for executor's process data */
    private final Map<String, Map<String, Object>> executorProcessDataNamespace;
    private final SprayProcessCoordinatorMeta coordinatorMeta;
    private final List<SprayExecutorListener> listeners;
    private final SprayData processData;
    private final LongAdder executingStepCounter;
    private final List<SprayData> defaultDataList;
    private final Thread creatorThread;

    private SprayPoolExecutor executor;

    private boolean executeOnlyOnce = true;

    public SprayDefaultProcessCoordinator(SprayProcessCoordinatorMeta coordinatorMeta) {
        this.cachedExecutorMap = new ConcurrentHashMap<>();
        this.executorProcessDataNamespace = new ConcurrentHashMap<>();
        this.coordinatorMeta = coordinatorMeta;
        this.listeners = new ArrayList<>();
        this.processData = new SprayData().keyBanned("stream", "globalVar");
        this.executingStepCounter = new LongAdder();
        this.defaultDataList = new ArrayList<>();
        this.creatorThread = Thread.currentThread();
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
        this.processData.clear();
        this.processData.putAll(SprayData.deepCopy(coordinatorMeta.getDefaultProcessData()));
        if (this.coordinatorMeta.getDefaultDataList() != null) {
            this.defaultDataList.addAll(this.coordinatorMeta.getDefaultDataList());
        }
        initThreadExecutor();
    }

    private void initThreadExecutor() {
        boolean asyncSupport = this.coordinatorMeta.isAsync();
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
        if (this.executingStepCounter.sum() != 0) {
            // running
            return SprayCoordinateStatus.RUNNING;
        }
        return calculateTheResult();
    }
    private SprayCoordinateStatus calculateTheResult() {
        for (Map.Entry<String, SprayProcessStepExecutor> stepExecutor : this.cachedExecutorMap.entrySet()) {
            // [STOP, FAILED, DONE, ERROR, PAUSED, RUNNING]
            SprayStatusType executorStatus = stepExecutor.getValue().getStepResult().getStatus();
            if (SprayStepStatus.FAILED.equals(executorStatus) || SprayStepStatus.ERROR.equals(executorStatus)) {
                return SprayCoordinateStatus.FAILED;
            }
            if (!SprayStepStatus.DONE.equals(executorStatus) && !SprayStepStatus.STOP.equals(executorStatus)) {
                return SprayCoordinateStatus.RUNNING;
            }
        }
        return SprayCoordinateStatus.SUCCESS;
    }


    @Override
    public void run() {
        boolean isAsyncRun = this.creatorThread != Thread.currentThread();
        if (this.defaultDataList.isEmpty()) {
            this.runNodes(this.getMeta().getStartNodes(),
                    null, null, null, false, isAsyncRun);
        } else {
            for (int i = 0; i < this.defaultDataList.size() - 1; i++) {
                this.runNodes(this.getMeta().getStartNodes(), null, null,
                        this.defaultDataList.get(i), false, isAsyncRun);
            }
            this.runNodes(this.getMeta().getStartNodes(), null, null,
                    this.defaultDataList.get(this.defaultDataList.size() - 1), true, isAsyncRun);
        }
    }

    private SprayPoolExecutor getSprayPoolExecutor(SprayProcessStepExecutor fromExecutor) {
        // TODO need a executor pool designed for the executor
        // if from executor contains info for its own executor then make one for it
        // else use the process executor
        return this.getSprayPoolExecutor();
    }

    @Override
    public void dispatch(SprayProcessStepExecutor fromExecutor, SprayNextStepFilter stepFilter,
                         SprayData data, boolean still, boolean dispatchAsync) {
        if (dispatchAsync) {
            SprayPoolExecutor poolExecutor = this.getSprayPoolExecutor(fromExecutor);
            poolExecutor.execute(() -> this.runNodes(fromExecutor.getMeta().nextNodes(), stepFilter, fromExecutor, data, still, dispatchAsync));
        } else {
            this.runNodes(fromExecutor.getMeta().nextNodes(), stepFilter, fromExecutor, data, still, dispatchAsync);
        }
    }

    @Override
    public void dispatchResult(SprayProcessStepExecutor fromExecutor, SprayData data, boolean still, boolean async, SprayProcessStepMeta nextMeta, SprayDataDispatchResultStatus dataDispatchStatus) {
        // TODO get data result save strategies
    }

    private boolean validBeforeCreate(SprayProcessStepExecutor fromExecutor, SprayNextStepFilter stepFilter, SprayData data, boolean still, boolean dispatchAsync, SprayProcessStepMeta nodeMeta) {
        boolean create = true;
        if (!SprayStepActiveType.ACTIVE.equals(nodeMeta.stepActiveType())) {
            if (SprayStepActiveType.IGNORE.equals(nodeMeta.stepActiveType())) {
                this.runNodes(nodeMeta.nextNodes(), stepFilter, fromExecutor, data, still, dispatchAsync);
            }
            this.dispatchResult(fromExecutor, data, still, dispatchAsync, nodeMeta, SprayDataDispatchResultStatus.SKIPPED);
            create = false;
        }
        if (stepFilter != null) {
            if (!stepFilter.canBeExecute(fromExecutor, data, still, nodeMeta)) {
                this.dispatchResult(fromExecutor, data, still, dispatchAsync, nodeMeta, SprayDataDispatchResultStatus.FILTERED);
                create = false;
            }
        }
        if (! nodeMeta.getExecuteConditionFilter().isEmpty()) {
            for (SprayStepExecuteConditionFilter filter : nodeMeta.getExecuteConditionFilter()) {
                if (!filter.filter(fromExecutor, data, still, nodeMeta)) {
                    create = false;
                    this.dispatchResult(fromExecutor, data, still, dispatchAsync, nodeMeta, SprayDataDispatchResultStatus.ABANDONED);
                    break;
                }
            }
        }
        return create;
    }

    private void runNodes(List<SprayProcessStepMeta> nodes, SprayNextStepFilter stepFilter, SprayProcessStepExecutor fromExecutor, SprayData data, boolean still, boolean dispatchAsync) {
        if (nodes == null || nodes.isEmpty()) {
            this.dispatchResult(fromExecutor, data, still, dispatchAsync, null, SprayDataDispatchResultStatus.ABANDONED);
            return;
        }
        List<CompletableFuture<Void>> futureResults = new ArrayList<>();
        for (SprayProcessStepMeta nodeMeta : nodes) {
            if (! validBeforeCreate(fromExecutor, stepFilter, data, still, dispatchAsync, nodeMeta)) {
                continue;
            }
            SprayProcessStepExecutor nextExecutor = SprayExecutorFactory.create(this, nodeMeta);
            if (nextExecutor.needWait(fromExecutor, data, still, this.getExecutorProcessData(fromExecutor, nextExecutor))) {
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
                        () -> this.executeNext(nextExecutor, fromExecutor, data, still),
                        this.getSprayPoolExecutor()));
            } else {
                this.executeNext(nextExecutor, fromExecutor, data, still);
            }
        }
        futureResults.forEach(CompletableFuture::join);
    }


    private void beforeExecute(SprayProcessStepExecutor nextExecutor,
                              SprayProcessStepExecutor fromExecutor,
                              SprayData data, boolean still) {
        this.beforeExecute0(nextExecutor, fromExecutor, data, still);
    }
    protected void beforeExecute0(SprayProcessStepExecutor nextExecutor,
                                 SprayProcessStepExecutor fromExecutor,
                                 SprayData data, boolean still) {

    }

    @Override
    public void executeNext(SprayProcessStepExecutor nextStepExecutor,
                                              SprayProcessStepExecutor fromExecutor,
                                              SprayData data, boolean still) {
        beforeExecute(nextStepExecutor, fromExecutor, data, still);
        try {
            this.executingStepCounter.increment();
            nextStepExecutor.execute(fromExecutor, data, still, this.getExecutorProcessData(fromExecutor, nextStepExecutor));
        } catch (Throwable e) {
            if (!nextStepExecutor.getMeta().ignoreError()) {
                nextStepExecutor.getStepResult().setStatus(SprayStepStatus.FAILED);
                nextStepExecutor.getStepResult().addError(e);
            }
        } finally {
            this.executingStepCounter.decrement();
        }
        afterExecute(nextStepExecutor, fromExecutor, data, still);
    }

    @Override
    public Map<String, Object> getExecutorProcessData(SprayProcessStepExecutor fromExecutor, SprayProcessStepExecutor curExecutor) {
        int copyMode = curExecutor.getMeta().varCopyMode();
        String curExecutorExecutorNameKey = curExecutor.getExecutorNameKey();
        Map<String, Object> executorProcessDataForUsing;
        // firstly get the base process data for cur executor
        if (fromExecutor == null) {
            // first executor in the executor line   ○ -> ○ -> ○
            //                                       ↑
            executorProcessDataForUsing = this.processData;
            if (copyMode == 0) {
                // not copy that it needs no namespace
                return executorProcessDataForUsing;
            }
            // try to get from namespace
            Map<String, Object> curExecutorProcessData = executorProcessDataNamespace.get(curExecutorExecutorNameKey);
            if (curExecutorProcessData != null) {
                return curExecutorProcessData;
            }
        } else {
            String lastExecutorExecutorNameKey = fromExecutor.getExecutorNameKey();
            if (copyMode == 0) {
                executorProcessDataForUsing = executorProcessDataNamespace.get(lastExecutorExecutorNameKey);
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
                Map<String, Object> curExecutorProcessData = executorProcessDataNamespace.get(curExecutorExecutorNameKey);
                if (curExecutorProcessData != null) {
                    return curExecutorProcessData;
                }
            } else {
                // copy mode

                // try to get from namespace
                Map<String, Object> curExecutorProcessData = executorProcessDataNamespace.get(curExecutorExecutorNameKey);
                if (curExecutorProcessData != null) {
                    return curExecutorProcessData;
                }

                executorProcessDataForUsing = executorProcessDataNamespace.get(lastExecutorExecutorNameKey);
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
                executorProcessDataForUsing = new HashMap<>(executorProcessDataForUsing);
            } else {
                // deep copy
                executorProcessDataForUsing = SprayData.deepCopy(executorProcessDataForUsing);
            }
        }
        // setting a namespace for cur executor
        executorProcessDataNamespace.put(curExecutorExecutorNameKey, executorProcessDataForUsing);
        return executorProcessDataForUsing;
    }

    protected void afterExecute(SprayProcessStepExecutor nextStepExecutor,
                                SprayProcessStepExecutor fromExecutor,
                                SprayData data, boolean still) {
        if (SprayStepStatus.DONE.equals(nextStepExecutor.getStepResult().getStatus())) {
            this.endUpWithExecutor(nextStepExecutor);
        }
    }
    private void endUpWithExecutor(SprayProcessStepExecutor executor) {
        if (executor instanceof SprayTransactionSupportExecutor transactionSupportExecutor) {
            if (SprayStepStatus.DONE.equals(executor.getStepResult().getStatus())) {
                transactionSupportExecutor.commit();
            } else if (SprayStepStatus.FAILED.equals(executor.getStepResult().getStatus()) ||
                    SprayStepStatus.STOP.equals(executor.getStepResult().getStatus())) {
                transactionSupportExecutor.rollback();
            }
        }
        if (executor instanceof SprayCloseableExecutor closeableExecutor) {
            try {
                closeableExecutor.close();
            } catch (Throwable closeException) {
                closeableExecutor.closeFailed(closeException);
            }
        }
    }

    @Override
    public void close() throws IOException {

    }
}
