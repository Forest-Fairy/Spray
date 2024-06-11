package top.spray.engine.coordinate.coordinator;

import top.spray.core.engine.execute.SprayListenable;
import top.spray.core.engine.result.SprayStepResult;
import top.spray.core.engine.props.SprayData;
import top.spray.core.engine.result.SprayCoordinateResult;
import top.spray.engine.step.executor.SprayExecutorListener;
import top.spray.engine.coordinate.meta.SprayProcessCoordinatorMeta;
import top.spray.engine.step.executor.SprayProcessStepExecutor;
import top.spray.engine.step.instance.SprayStepResultInstance;
import top.spray.engine.step.meta.SprayProcessStepMeta;

import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Stream;

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
    public SprayCoordinateResult execute() {
        ExecutorService executorService = null;
        if (this.coordinatorMeta.minThreadCount() > 1) {
            executorService = new ThreadPoolExecutor(
                    this.coordinatorMeta.minThreadCount(),
                    Integer.MAX_VALUE, 3, TimeUnit.SECONDS, new LinkedBlockingQueue<>(this.coordinatorMeta.minThreadCount())
            );
        }
        SprayCoordinateResult coordinateResult = SprayCoordinateResult.SUCCESS;
        List<CompletableFuture<SprayStepResultInstance<?>>> futureResults = new ArrayList<>();
        for (SprayProcessStepMeta startNode : this.coordinatorMeta.getStartNodes()) {
            if (startNode.isAsync() && executorService != null) {
                futureResults.add(CompletableFuture.supplyAsync(
                        () -> this.execute(startNode),
                        executorService));
            } else {
                SprayStepResult resultStatus = this.execute(startNode).getStatus();
                if (!SprayStepResult.DONE.equals(resultStatus)) {
                    return
                }
            }
        }
        return null;
    }


    @Override
    public Executor getExecutor() {
        return this.executor;
    }

    @Override
    public Map<String, Object> getProcessData() {
        return this.processData;
    }


    protected SprayStepResultInstance<?> execute(SprayProcessStepMeta stepMeta) {
        SprayProcessStepExecutor executor = SprayProcessStepExecutor.create(this, stepMeta);
        executor.run();
        return executor.getStepResult();
    }

}
