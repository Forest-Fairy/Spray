package top.spray.engine.step.instance;

import top.spray.core.engine.props.SprayData;
import top.spray.core.engine.types.SprayType;
import top.spray.core.engine.types.SprayTypeHolder;
import top.spray.core.engine.types.data.execute.record.SprayExecutionRecordType;
import top.spray.core.dynamic.loader.SprayClassLoader;
import top.spray.engine.coordinate.meta.SprayProcessCoordinatorMeta;
import top.spray.core.engine.types.step.status.SprayStepStatus;
import top.spray.engine.step.handler.record.SprayExecutionRecordHandler;
import top.spray.engine.step.meta.SprayProcessStepMeta;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.LongAdder;

/**
 * the step status instance
 *  - create by an executor with the only instance
 */
public class SprayStepResultInstance {
    private final SprayProcessCoordinatorMeta coordinatorMeta;
    private final SprayProcessStepMeta executorMeta;
    private final SprayClassLoader classLoader;
    private SprayTypeHolder stepStatus;
    private final LongAdder runningCounter;
    private List<SprayExecutionRecordHandler> executionStrategyHandlers;
    private Map<String, LongAdder> inputInfos;
    private Map<String, LongAdder> outputInfos;
    private long startTime;
    private long endTime;

    public SprayStepResultInstance(SprayProcessCoordinatorMeta coordinatorMeta,
                                   SprayProcessStepMeta executorMeta,
                                   SprayClassLoader classLoader) {
        this.coordinatorMeta = coordinatorMeta;
        this.executorMeta = executorMeta;
        this.classLoader = classLoader;
        this.runningCounter = new LongAdder();
        init();
    }
    private void init() {
        this.stepStatus = SprayType.holder(SprayStepStatus.RUNNING);
        this.startTime = System.currentTimeMillis();
        this.inputInfos = new ConcurrentHashMap<>(0);
        this.outputInfos = new ConcurrentHashMap<>(0);
        this.executionStrategyHandlers = SprayExecutionRecordHandler.create(coordinatorMeta, executorMeta);
    }

    public SprayProcessCoordinatorMeta getCoordinatorMeta() {
        return this.coordinatorMeta;
    }

    public SprayProcessStepMeta getExecutorMeta() {
        return this.executorMeta;
    }

    public String transactionId() {
        return coordinatorMeta.transactionId();
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

//    public void addError(Throwable error) {
//        this.errorList.add(Pair.of(System.currentTimeMillis(), error));
//    }

    /**
     * record before the executor being executed with this data
     * @param fromExecutorMeta dataFromExecutor
     * @param data data
     * @param still still
     */
    public void recordInputBeforeExecution(SprayProcessStepMeta fromExecutorMeta, SprayData data, boolean still) {
        this.executionStrategyHandlers.forEach(handler -> handler.record(
                executorMeta, SprayExecutionRecordType.RECORD_BEFORE_EXECUTE, fromExecutorMeta, data, still, null));
    }

    /**
     * record after the executor being executed successfully
     * @param fromExecutorMeta dataFromExecutor
     * @param data data
     * @param still still
     */
    public void recordInputAfterExecutionSuccess(SprayProcessStepMeta fromExecutorMeta, SprayData data, boolean still) {
        this.executionStrategyHandlers.forEach(handler -> handler.record(
                executorMeta, SprayExecutionRecordType.RECORD_EXECUTE_SUCCESS, fromExecutorMeta, data, still, null));
    }

    /**
     * record after the executor being executed failed
     * @param fromExecutorMeta dataFromExecutor
     * @param data data
     * @param still still
     */
    public void recordInputAfterExecutionFailed(SprayProcessStepMeta fromExecutorMeta, SprayData data, boolean still, Throwable error) {
        this.executionStrategyHandlers.forEach(handler -> handler.record(
                executorMeta, SprayExecutionRecordType.RECORD_EXECUTE_FAILED, fromExecutorMeta, data, still, error));
    }

    public void endStep() {
        this.endTime = System.currentTimeMillis();
    }


    public SprayType getStatus() {
        return this.stepStatus;
    }
    public long getStartTime() {
        return this.startTime;
    }
    public long getEndTime() {
        return this.endTime;
    }
    public long duration() {
        return this.endTime == 0 ?
                System.currentTimeMillis() - this.startTime :
                this.endTime - this.startTime;
    }
}
