package top.spray.engine.coordinate.coordinator;

import top.spray.engine.base.execute.SprayListenable;
import top.spray.engine.base.instance.SprayStepInstanceStatus;
import top.spray.engine.base.props.SprayData;
import top.spray.engine.base.result.SprayCoordinateResult;
import top.spray.engine.step.executor.SprayExecutorListener;
import top.spray.engine.coordinate.meta.SprayProcessCoordinatorMeta;
import top.spray.engine.step.executor.SprayProcessStepExecutor;
import top.spray.engine.step.instance.SprayStepResultInstance;
import top.spray.engine.step.meta.SprayProcessStepMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.*;
import java.util.stream.Stream;

public class SprayMetaDriveProcessCoordinator implements
        SprayProcessCoordinator,
        SprayListenable<SprayExecutorListener> {

    private final SprayProcessCoordinatorMeta coordinatorMeta;
    private final List<SprayExecutorListener> listeners;


    public SprayMetaDriveProcessCoordinator(SprayProcessCoordinatorMeta coordinatorMeta) {
        this.coordinatorMeta = coordinatorMeta;
        this.listeners = new ArrayList<>();
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
        List<CompletableFuture<SprayStepResultInstance<?>>> futureResults = new ArrayList<>();
        for (SprayProcessStepMeta startNode : this.coordinatorMeta.getStartNodes()) {
            if (startNode.isAsync() && executorService != null) {
                futureResults.add(CompletableFuture.supplyAsync(
                        () -> this.executeOneByOne(startNode),
                        executorService));
            } else {
                SprayStepInstanceStatus resultStatus = this.executeOneByOne(startNode).getStatus();
                if (SprayStepInstanceStatus.DONE)
            }
        }
        return null;
    }


    protected SprayStepResultInstance<?> executeOneByOne(SprayProcessStepMeta stepMeta) {
        boolean gotoNext = true;
        try {
            // TODO cast to publish model
            SprayStepResultInstance<?> sprayStepResultInstance = this.executeOne(stepMeta);
            if (SprayStepInstanceStatus.DONE)
        } catch (Throwable throwable) {
            if (!stepMeta.ignoreError()) {
                gotoNext = false;
                // need to implement an error result instance
            }
        }
        if (gotoNext) {
            List<SprayProcessStepMeta> sprayProcessStepMetas = stepMeta.nextNodes();
            for (SprayProcessStepMeta sprayProcessStepMeta : sprayProcessStepMetas) {
                this.executeOneByOne(sprayProcessStepMeta);
            }
        }
    }

    protected SprayStepResultInstance<?> executeOne(SprayProcessStepMeta stepMeta) {
        SprayProcessStepExecutor executor = SprayProcessStepExecutor.create(this, stepMeta);
        // TODO make it a publish model
        SprayStepResultInstance<? extends SprayProcessStepExecutor> stepResult = null;
        executor.execute(this, stepMeta,
                new SprayData(this.coordinatorMeta.getDefaultProcessData()),
                Stream.empty(), stepResult);
        return stepResult;
    }

}
