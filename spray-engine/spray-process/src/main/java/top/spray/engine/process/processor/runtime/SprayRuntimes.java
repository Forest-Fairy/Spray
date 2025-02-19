package top.spray.engine.process.processor.runtime;

import top.spray.engine.process.processor.dispatch.coordinate.coordinator.SprayProcessCoordinator;
import top.spray.engine.process.processor.dispatch.coordinate.status.SprayCoordinatorStatus;
import top.spray.engine.process.processor.execute.step.executor.facade.SprayStepFacade;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.LongAdder;

public class SprayRuntimes {
    private static final Map<String, SprayRuntimes> RUNNING_COORDINATORS = new ConcurrentHashMap<>();
    private static final ThreadLocal<SprayRuntimes> runtimes = new ThreadLocal<>();

    @Nullable
    public static SprayRuntimes GetRuntimes(SprayProcessCoordinator coordinator, boolean create) {
        SprayRuntimes sprayRuntimes = RUNNING_COORDINATORS.get(coordinator.transactionId());
        if (sprayRuntimes == null && create) {
            sprayRuntimes = new SprayRuntimes(coordinator);
        }
        return sprayRuntimes;
    }

    private final SprayProcessCoordinator coordinator;
    private SprayStepFacade executorFacade;
    private final Map<String, LongAdder> executorRunningCounter;
    private SprayRuntimes(SprayProcessCoordinator coordinator) {
        this.coordinator = coordinator;
        this.executorFacade = null;
        this.executorRunningCounter = new HashMap<>();
        RUNNING_COORDINATORS.put(coordinator.transactionId(), this);
    }

    @Nonnull
    public SprayProcessCoordinator getCoordinator() {
        return coordinator;
    }

    @Nullable
    public SprayStepFacade getExecutorFacade() {
        return executorFacade;
    }

    public void start(@Nonnull SprayStepFacade executorFacade) {
        executorRunningCounter.computeIfAbsent(executorFacade.executorNameKey(), key -> new LongAdder()).increment();
        this.executorFacade = executorFacade;
    }
    public void close(@Nonnull SprayStepFacade executorFacade) {
        executorRunningCounter.computeIfAbsent(executorFacade.executorNameKey(), key -> new LongAdder()).decrement();
        SprayCoordinatorStatus coordinatorStatus = coordinator.runningStatus().getStatus();
        if (coordinatorStatus.isEnd()) {
            shut();
        }
        this.executorFacade = null;
    }

    private void shut() {
        if (executorRunningCounter.values().stream().map(LongAdder::longValue).anyMatch(c -> c>0)) {
            return;
        }
        runtimes.remove();
    }
}
