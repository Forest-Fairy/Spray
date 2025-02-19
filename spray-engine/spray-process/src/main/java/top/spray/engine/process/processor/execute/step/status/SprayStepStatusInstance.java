package top.spray.engine.process.processor.execute.step.status;

import top.spray.core.type.SprayType;
import top.spray.core.type.SprayTypeHolder;
import top.spray.engine.process.infrastructure.listen.SprayListenable;
import top.spray.engine.process.infrastructure.listen.SprayListener;
import top.spray.engine.process.processor.dispatch.coordinate.coordinator.SprayProcessCoordinator;
import top.spray.engine.process.processor.execute.step.executor.facade.SprayStepFacade;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.LongAdder;

/**
 * the coordinator's status instance
 */
public class SprayStepStatusInstance implements SprayListenable {
    private final SprayProcessCoordinator coordinator;
    private final SprayStepFacade executorFacade;
    private SprayTypeHolder<SprayStepStatus> stepStatus;
    private LongAdder runningCounter;
    private List<SprayListener> statusEventListeners;
    private Map<String, LongAdder> inputInfos;
    private Map<String, LongAdder> outputInfos;
    private long startTime;
    private long endTime;

    public SprayStepStatusInstance(SprayProcessCoordinator coordinator,
                                   SprayStepFacade executorFacade) {
        this.runningCounter = new LongAdder();
        this.coordinator = coordinator;
        this.executorFacade = executorFacade;
        init();
    }
    private void init() {
        this.stepStatus = SprayType.holder(SprayStepStatus.WAITING);
        this.startTime = System.currentTimeMillis();
        this.inputInfos = new ConcurrentHashMap<>(0);
        this.outputInfos = new ConcurrentHashMap<>(0);
        this.statusEventListeners = SprayExecutorStatusEventListenerFactory.getListeners(this);
    }

    public SprayProcessCoordinator getCoordinator() {
        return this.coordinator;
    }

    public SprayStepFacade getExecutorFacade() {
        return this.executorFacade;
    }

    public String transactionId() {
        return this.coordinator.getMeta().getTransactionId();
    }


    public long runningCount() {
        return this.runningCounter.sum();
    }
    public void incrementConsumingCount() {
        this.runningCounter.increment();
    }
    public void decrementConsumingCount() {
        this.runningCounter.decrement();
    }



    public Map<String, LongAdder> inputInfos() {
        return this.inputInfos;
    }
    public void addInputInfo(String infoKey, long count) {
        this.inputInfos.computeIfAbsent(infoKey, key -> new LongAdder())
                .add(count);
    }

    public Map<String, LongAdder> outputInfos() {
        return this.outputInfos;
    }
    public void addOutputInfo(String infoKey, long count) {
        this.outputInfos.computeIfAbsent(infoKey, key -> new LongAdder())
                .add(count);
    }

    public void setStatus(SprayStepStatus status) {
        this.stepStatus.set(status);
    }
    public void endStep(SprayStepStatus status) {
        this.setStatus(status);
        this.endTime = System.currentTimeMillis();
    }


    public SprayStepStatus getStatus() {
        return SprayStepStatus.get(this.stepStatus.getCode());
    }
    public long getStartTime() {
        return this.startTime;
    }
    public long getEndTime() {
        return this.endTime;
    }
    public long getDuration() {
        return this.endTime == 0 ?
                System.currentTimeMillis() - this.startTime :
                this.endTime - this.startTime;
    }

    @Override
    public boolean listenerRegister(SprayListener listener) {
        if (listener instanceof SprayExecutorStatusEventListener) {
            this.statusEventListeners.add((SprayExecutorStatusEventListener) listener);
            return true;
        }
        return false;
    }
}
