package top.spray.engine.coordinate.coordinator;

import com.fasterxml.jackson.databind.ObjectMapper;
import top.spray.core.engine.execute.SprayListenable;
import top.spray.core.engine.props.SprayData;
import top.spray.core.engine.result.SprayStepStatus;
import top.spray.core.engine.result.SprayCoordinateStatus;
import top.spray.engine.step.executor.SprayExecutorListener;
import top.spray.engine.coordinate.meta.SprayProcessCoordinatorMeta;
import top.spray.engine.step.executor.SprayProcessStepExecutor;
import top.spray.engine.step.executor.closeable.SprayCloseableExecutor;
import top.spray.engine.step.executor.factory.SprayExecutorFactory;
import top.spray.engine.step.executor.transaction.SprayTransactionSupportExecutor;
import top.spray.engine.step.instance.SprayStepResultInstance;
import top.spray.engine.step.meta.SprayProcessStepMeta;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.LongAdder;

public class SprayMetaDriveProcessCoordinator implements
        SprayProcessCoordinator,
        SprayListenable<SprayExecutorListener> {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final SprayProcessCoordinatorMeta coordinatorMeta;
    private final List<SprayExecutorListener> listeners;
    private final Map<String, Object> processData;
    private final LongAdder counter;
    private final Map<String, SprayStepResultInstance<?>> executorResultMap;

    private ThreadPoolExecutor executor;



    public SprayMetaDriveProcessCoordinator(SprayProcessCoordinatorMeta coordinatorMeta) {
        this.coordinatorMeta = coordinatorMeta;
        this.listeners = new ArrayList<>();
        this.processData = new HashMap<>();
        this.counter = new LongAdder();
        this.executorResultMap = new ConcurrentHashMap<>();
        init();
    }

    public void init() {
        while (this.counter.sum() > 0) {
            Thread.yield();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(new Exception(
                        "failed to init the coordinator for current thread was interrupted", e));
            }
        }
        initVar();
        initThreadExecutor();
    }

    private void initVar() {
        this.executorResultMap.clear();
        this.processData.clear();
        this.processData.putAll(SprayData.deepCopy(coordinatorMeta.getDefaultProcessData()));
    }

    private void initThreadExecutor() {
        boolean asyncSupport = this.coordinatorMeta.isAsync();
        if (asyncSupport) {
            if (this.coordinatorMeta.minThreadCount() > 1) {
                this.executor = new ThreadPoolExecutor(
                        this.coordinatorMeta.minThreadCount(),
                        Integer.MAX_VALUE,
                        3, TimeUnit.SECONDS,
                        new LinkedBlockingQueue<>(this.coordinatorMeta.minThreadCount())
                );
            } else {
                this.executor = new ThreadPoolExecutor(
                        0, Integer.MAX_VALUE,
                        3, TimeUnit.SECONDS,
                        new LinkedBlockingQueue<>()
                );
            }
        } else {
            this.executor = null;
        }
    }

    @Override
    public SprayProcessCoordinatorMeta getMeta() {
        return coordinatorMeta;
    }


    @Override
    public SprayMetaDriveProcessCoordinator addListener(SprayExecutorListener... listeners) {
        this.listeners.addAll(Arrays.asList(listeners));
        return this;
    }

    @Override
    public List<SprayExecutorListener> getListeners() {
        return this.listeners;
    }





    @Override
    public Executor getThreadExecutor() {
        return this.executor;
    }

    @Override
    public void run() {
        this.runNodes(this.getMeta().getStartNodes(),
                null, null, false);
    }
    @Override
    public void dispatch(List<SprayProcessStepMeta> nodes,
                         SprayProcessStepExecutor fromExecutor,
                         SprayData data, boolean still) {
        this.runNodes(nodes, fromExecutor, data, still);
    }

    @Override
    public SprayCoordinateStatus getResult() {
        if (this.counter.sum() != 0) {
            // running
            return SprayCoordinateStatus.RUNNING;
        }
        return calculateTheResult();
    }

    private SprayCoordinateStatus calculateTheResult() {
        this.executorResultMap.entrySet().stream().filter(entry -> {
            entry.getValue().getStatus().equals(SprayStepStatus.FAILED)
        });

    }

    @Override
    public Map<String, Object> getProcessData() {
        return this.processData;
    }

    @Override
    public void beforeExecute(SprayProcessStepExecutor executor) {}

    @Override
    public SprayStepResultInstance<?> execute(SprayProcessStepExecutor stepExecutor) {
        try {
            stepExecutor.run();
        } catch (Throwable e) {
            if (!stepExecutor.getMeta().ignoreError()) {
                stepExecutor.getStepResult().setStatus(SprayStepStatus.FAILED);
                stepExecutor.getStepResult().setError(e);
            }
        }
        postExecute(stepExecutor);
        return stepExecutor.getStepResult();
    }

    @Override
    public void postExecute(SprayProcessStepExecutor executor) {
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


    private void runNodes(List<SprayProcessStepMeta> nodes, SprayProcessStepExecutor fromExecutor, SprayData data, boolean still) {
        this.counter.increment();
        try {

            List<CompletableFuture<SprayStepResultInstance<?>>> futureResults = new ArrayList<>();
            for (SprayProcessStepMeta nodeMeta : nodes) {
                SprayProcessStepExecutor nodeExecutor = SprayExecutorFactory.create(this, nodeMeta);
                if (nodeExecutor.needWait(fromExecutor, data, still)) {
                    // the executor need to wait for all data
                    nodeExecutor.dataInput(fromExecutor, data, still);
                    continue;
                }
                if (fromExecutor != null) {
                    // if fromExecutor is null means no data need for the executor to run
                    // send data to next executor
                    nodeExecutor.dataInput(fromExecutor, data, still);
                }
                // run it
                {
                    // TODO check the last data run after all before sending data...
                }
                if (nodeMeta.isAsync() && this.getThreadExecutor() != null) {
                    futureResults.add(CompletableFuture.supplyAsync(
                            // this will create a new thread to run the step fully
                            () -> this.execute(nodeExecutor),
                            this.getThreadExecutor()));
                } else {
                    this.executorResultMap.put(nodeExecutor.getExecutorId(), this.execute(nodeExecutor));
                }
            }
            futureResults.forEach(future -> {
                SprayStepResultInstance<?> result = future.join();
                this.executorResultMap.put(result.getExecutor().getExecutorId(), result);
            });
        } finally {
            this.counter.decrement();
        }
    }
}
