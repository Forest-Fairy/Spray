package top.spray.engine.step.instance;

import org.apache.commons.lang3.tuple.Pair;
import top.spray.core.engine.execute.SprayMetaDrive;
import top.spray.core.engine.props.SprayData;
import top.spray.core.engine.result.SprayStatusType;
import top.spray.core.engine.result.SprayStatusHolder;
import top.spray.engine.coordinate.coordinator.SprayProcessCoordinator;
import top.spray.engine.step.executor.SprayProcessStepExecutor;
import top.spray.core.engine.result.SprayStepStatus;
import top.spray.engine.step.meta.SprayProcessStepMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.LongAdder;

/**
 * the step result instance
 *  - create by an executor with the only instance
 */
public class SprayStepResultInstance implements SprayMetaDrive<SprayProcessStepMeta> {
    private final SprayProcessCoordinator coordinator;
    private final SprayProcessStepExecutor executor;
    private final SprayProcessStepMeta stepMeta;
    private SprayStatusHolder stepStatus;
    /**
     * ['NONE', 'ALL', 'SUCCESS', 'BAD']
     *  - NONE means not record
     *  - ALL means record all
     *  - SUCCESS means record success only
     *  - BAD means record the error or failed
     */
    private String dataRecordStrategy;
    private List<Pair<Long, Throwable>> errorList;
    private Map<String, LongAdder> inputInfos;
    private Map<String, LongAdder> outputInfos;
    private LongAdder dataProcessingCounter;
    private long startTime;
    private long endTime;

    public SprayStepResultInstance(SprayProcessCoordinator coordinator, SprayProcessStepExecutor executor) {
        this.coordinator = coordinator;
        this.executor = executor;
        this.stepMeta = executor.getMeta();
        init();
    }
    private void init() {
        this.stepStatus = SprayStatusType.create(SprayStepStatus.RUNNING);
        this.errorList = new ArrayList<>(0);
        this.inputInfos = new ConcurrentHashMap<>(0);
        this.outputInfos = new ConcurrentHashMap<>(0);
        this.dataRecordStrategy = executor.getMeta()
                .getString("dataRecordStrategy", "NONE").toUpperCase();
    }
    public SprayProcessStepExecutor getExecutor() {
        return this.executor;
    }

    @Override
    public SprayProcessStepMeta getMeta() {
        return this.stepMeta;
    }

    public String transactionId() {
        return coordinator.getMeta().transactionId();
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
        this.stepStatus.setStatus(status);
    }
    public SprayStatusType getStatus() {
        return this.stepStatus;
    }

    public void addError(Throwable error) {
        this.errorList.add(Pair.of(System.currentTimeMillis(), error));
    }

    /**
     * record before the executor being executed with this data
     * @param status the snapshot of current instance's status
     * @param fromExecutor dataFromExecutor
     * @param data data
     * @param still still
     */
    public void recordBeforeExecute(SprayStepStatus status, SprayProcessStepExecutor fromExecutor, SprayData data, boolean still) {
        dataProcessingCounter.increment();
    }

    /**
     * record after the executor being executed
     * @param status the snapshot of current instance's status
     * @param fromExecutor dataFromExecutor
     * @param data data
     * @param still still
     */
    public void recordInputAgain(SprayStepStatus status, SprayProcessStepExecutor fromExecutor, SprayData data, boolean still) {
        dataProcessingCounter.decrement();
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }
    public long getStartTime() {
        return this.startTime;
    }
    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }
    public long getEndTime() {
        return this.endTime;
    }


    public long duration() {
        return this.endTime == 0 ? 0 :
                this.endTime - this.startTime;
    }
}
