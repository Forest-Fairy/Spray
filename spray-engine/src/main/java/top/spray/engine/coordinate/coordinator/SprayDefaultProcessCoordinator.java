package top.spray.engine.coordinate.coordinator;

import top.spray.core.engine.exception.SprayNotSupportError;
import top.spray.core.engine.execute.SprayListenable;
import top.spray.core.engine.execute.SprayStepActiveType;
import top.spray.core.engine.props.SprayData;
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
import top.spray.engine.step.executor.storage.SprayFileStorageSupportExecutor;
import top.spray.engine.step.executor.transaction.SprayTransactionSupportExecutor;
import top.spray.engine.step.instance.SprayStepResultInstance;
import top.spray.engine.step.meta.SprayProcessStepMeta;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.LongAdder;
import java.util.stream.Collectors;

public class SprayDefaultProcessCoordinator implements
        SprayProcessCoordinator,
        SprayListenable<SprayExecutorListener> {
    private final Map<String, SprayProcessStepExecutor> cachedExecutorMap = new ConcurrentHashMap<>();
    private final Map<String, LongAdder> executorCounterMap = new ConcurrentHashMap<>();
    private final SprayProcessCoordinatorMeta coordinatorMeta;
    private final List<SprayExecutorListener> listeners;
    private final SprayData processData;
    private final LongAdder executingStepCounter;
    private final Map<String, SprayStepResultInstance> executorResultMap;
    private final List<SprayData> defaultDataList;

    private SprayPoolExecutor executor;

    private boolean executeOnlyOnce = true;

    public SprayDefaultProcessCoordinator(SprayProcessCoordinatorMeta coordinatorMeta) {
        this.coordinatorMeta = coordinatorMeta;
        this.listeners = new ArrayList<>();
        this.processData = new SprayData().keyBanned("stream", "globalVar");
        this.executingStepCounter = new LongAdder();
        this.executorResultMap = new ConcurrentHashMap<>();
        this.defaultDataList = new ArrayList<>();
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
                    new IllegalAccessError("the coordinator can be run once only"));
        }
        executeOnlyOnce = false;
        initVar();
    }

    private void initVar() {
        this.executorResultMap.clear();
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
    public Map<String, Object> getProcessData() {
        return this.processData;
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
        if (this.executorResultMap.entrySet().stream()
                .anyMatch(entry -> SprayStepStatus.FAILED.equals(entry.getValue().getStatus()))) {
            return SprayCoordinateStatus.FAILED;
        } else {
            return SprayCoordinateStatus.SUCCESS;
        }
    }


    @Override
    public void run() {
        if (this.defaultDataList.isEmpty()) {
            this.runNodes(this.getMeta().getStartNodes(),
                    null, null, false);
        } else {
            for (int i = 0; i < this.defaultDataList.size() - 1; i++) {
                this.runNodes(this.getMeta().getStartNodes(), null,
                        this.defaultDataList.get(i), false);
            }
            this.runNodes(this.getMeta().getStartNodes(), null,
                    this.defaultDataList.get(this.defaultDataList.size() - 1), true);
        }
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

    private SprayPoolExecutor getSprayPoolExecutor(SprayProcessStepExecutor fromExecutor) {
        // TODO a executor pool designed for the executor
        // if from executor contains info for its own executor then make one for it
        // else use the process executor
        return this.getSprayPoolExecutor();
    }


    private void runNodes(List<SprayProcessStepMeta> nodes, SprayNextStepFilter stepFilter, SprayProcessStepExecutor fromExecutor, SprayData data, boolean still, boolean dispatchAsync) {
        if (nodes == null || nodes.isEmpty()) {
            return;
        }
        List<CompletableFuture<SprayStepResultInstance>> futureResults = new ArrayList<>();
        for (SprayProcessStepMeta nodeMeta : nodes) {
            if (stepFilter != null) {
                if (!stepFilter.canBeExecute(fromExecutor, data, still, nodeMeta)) {
                    continue;
                }
            }
            if (! beforeCreateValid(fromExecutor, data, still, dispatchAsync, nodeMeta)) {
                continue;
            }
            SprayProcessStepExecutor nextExecutor = SprayExecutorFactory.create(this, nodeMeta);
            // if the executor support to storage data in file then try
            if (nextExecutor instanceof SprayFileStorageSupportExecutor storageSupportExecutor) {
                if (storageSupportExecutor.timeToStorageInFile(fromExecutor, data, still)) {
                    storageSupportExecutor.storageInFile(fromExecutor, data, still);
                }
            }
            if (nextExecutor.needWait(fromExecutor, data, still, this.getProcessData(fromExecutor))) {
                // the executor need to wait
                continue;
            }
            String executorNameKey = nextExecutor.getExecutorNameKey();
            // run it
            if (nodeMeta.isAsync() && this.getSprayPoolExecutor() != null) {
                {
                    // TODO check and ensure that the last data run after all before sending data...
                }
                futureResults.add(CompletableFuture.supplyAsync(
                        // this will create a new thread to run the step fully
                        () -> this.executeNext(nextExecutor, fromExecutor, data, still),
                        this.getSprayPoolExecutor()));
            } else {
                this.executorResultMap.put(executorNameKey, this.executeNext(nextExecutor, fromExecutor, data, still));
            }
        }
        futureResults.forEach(future -> {
            SprayStepResultInstance result = future.join();
            this.executorResultMap.put(result.getExecutor().getExecutorNameKey(), result);
        });
    }

    private boolean beforeCreateValid(SprayProcessStepExecutor fromExecutor, SprayData data, boolean still, boolean async, SprayProcessStepMeta nodeMeta) {
        boolean create = true;
        if (!SprayStepActiveType.ACTIVE.equals(nodeMeta.stepActiveType())) {
            if (SprayStepActiveType.IGNORE.equals(nodeMeta.stepActiveType())) {
                this.runNodes(nodeMeta.nextNodes(), fromExecutor, data, still);
            }
            this.dispatchResult(fromExecutor, data, still, async, nodeMeta, SprayDataDispatchResultStatus.SKIPPED);
            create = false;
        }
        if (!nodeMeta.getExecuteConditionFilter().isEmpty()) {
            for (SprayStepExecuteConditionFilter filter : nodeMeta.getExecuteConditionFilter()) {
                if (!filter.filter(fromExecutor, data, still, nodeMeta)) {
                    create = false;
                    this.dispatchResult(fromExecutor, data, still, async, nodeMeta, SprayDataDispatchResultStatus.FILTERED);
                    break;
                }
            }
        }
        return create;
    }


    protected void beforeExecute(SprayProcessStepExecutor nextExecutor,
                              SprayProcessStepExecutor fromExecutor,
                              SprayData data, boolean still) {

    }

    @Override
    public SprayStepResultInstance executeNext(SprayProcessStepExecutor nextStepExecutor,
                                              SprayProcessStepExecutor fromExecutor,
                                              SprayData data, boolean still) {
        beforeExecute(nextStepExecutor, fromExecutor, data, still);
        try {
            this.executingStepCounter.increment();
            nextStepExecutor.execute(fromExecutor, data, still, this.getProcessData(fromExecutor));
        } catch (Throwable e) {
            if (!nextStepExecutor.getMeta().ignoreError()) {
                nextStepExecutor.getStepResult().setStatus(SprayStepStatus.FAILED);
                nextStepExecutor.getStepResult().addError(e);
            }
        } finally {
            this.executingStepCounter.decrement();
        }
        afterExecute(nextStepExecutor, fromExecutor, data, still);
        return nextStepExecutor.getStepResult();
    }

    private Map<String, Object> getProcessData(SprayProcessStepExecutor fromExecutor) {
        return fromExecutor == null ? this.processData : fromExecutor.getProcessData();
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
