package top.spray.engine.coordinate.coordinator;

import top.spray.core.engine.execute.SprayListenable;
import top.spray.core.engine.result.SprayStepStatus;
import top.spray.core.engine.result.SprayCoordinateStatus;
import top.spray.engine.step.executor.SprayExecutorListener;
import top.spray.engine.coordinate.meta.SprayProcessCoordinatorMeta;
import top.spray.engine.step.executor.SprayProcessStepExecutor;
import top.spray.engine.step.instance.SprayStepResultInstance;
import top.spray.engine.step.meta.SprayProcessStepMeta;

import java.util.*;
import java.util.concurrent.*;

public class SprayMetaDriveProcessCoordinator implements
        SprayProcessCoordinator,
        SprayListenable<SprayExecutorListener> {

    private final SprayProcessCoordinatorMeta coordinatorMeta;
    private final List<SprayExecutorListener> listeners;
    private final Map<String, Object> processData;

    private ThreadPoolExecutor executor;

    public SprayMetaDriveProcessCoordinator(SprayProcessCoordinatorMeta coordinatorMeta) {
        this.coordinatorMeta = coordinatorMeta;
        this.listeners = new ArrayList<>();
        this.processData = new HashMap<>(coordinatorMeta.getDefaultProcessData());
        synchronizedInit();
    }

    private void synchronizedInit() {
        initExecutor();
    }

    private void initExecutor() {
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
    public SprayCoordinateStatus execute() {
        SprayCoordinateStatus coordinateResult = SprayCoordinateStatus.SUCCESS;
        List<CompletableFuture<SprayStepResultInstance<?>>> futureResults = new ArrayList<>();
        Map<String, SprayStepResultInstance<?>> executorResultMap = new HashMap<>();
        for (SprayProcessStepMeta startNode : this.coordinatorMeta.getStartNodes()) {
            if (startNode.isAsync() && this.getThreadExecutor() != null) {
                futureResults.add(CompletableFuture.supplyAsync(
                        // this will create a new thread to run the step fully
                        () -> this.execute(startNode),
                        this.getThreadExecutor()));
            } else {
                String executorId = SprayProcessStepExecutor.getExecutorId(this, startNode);
                executorResultMap.put(executorId, this.execute(startNode));
            }
        }
        // collect the result by async running
        for (CompletableFuture<SprayStepResultInstance<?>> futureResult : futureResults) {
            SprayStepResultInstance<?> stepResultInstance = futureResult.join();
            String executorId = SprayProcessStepExecutor.getExecutorId(this, stepResultInstance.getExecutor().getMeta());
            executorResultMap.put(executorId, stepResultInstance);
        }
        executorResultMap

        return coordinateResult;
    }


    @Override
    public Executor getThreadExecutor() {
        return this.executor;
    }

    @Override
    public Map<String, Object> getProcessData() {
        return this.processData;
    }


    protected SprayStepResultInstance<?> execute(SprayProcessStepMeta stepMeta) {
        SprayProcessStepExecutor executor = SprayProcessStepExecutor.create(this, stepMeta);
        try {
            executor.run();
        } catch (Throwable e) {
            if (!executor.getMeta().ignoreError()) {
                executor.getStepResult().setStatus(SprayStepStatus.FAILED);
                executor.getStepResult().setError(e);
            }
        }
        return executor.getStepResult();
    }

}
