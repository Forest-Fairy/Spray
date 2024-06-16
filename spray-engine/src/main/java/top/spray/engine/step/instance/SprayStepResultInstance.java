package top.spray.engine.step.instance;

import org.apache.commons.lang3.tuple.Pair;
import top.spray.core.engine.props.SprayData;
import top.spray.engine.coordinate.coordinator.SprayProcessCoordinator;
import top.spray.engine.step.executor.SprayProcessStepExecutor;
import top.spray.core.engine.result.SprayStepStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * the step result instance
 *  - create by an executor with the only instance
 */
public class SprayStepResultInstance<Executor extends SprayProcessStepExecutor> {
    private final SprayProcessCoordinator coordinator;
    private final Executor executor;
    private SprayStepStatus stepStatus;
    /**
     * ['NONE', 'ALL', 'SUCCESS', 'BAD']
     *  - NONE means not record
     *  - ALL means record all
     *  - SUCCESS means record success only
     *  - BAD means record the error or failed
     */
    private String dataRecordStrategy;
    private List<Pair<Long, Throwable>> errorList;


    public SprayStepResultInstance(SprayProcessCoordinator coordinator, Executor executor) {
        this.coordinator = coordinator;
        this.executor = executor;
        init();
    }
    private void init() {
        this.stepStatus = SprayStepStatus.RUNNING;
        this.errorList = new ArrayList<>();
        this.dataRecordStrategy = executor.getMeta()
                .getString("dataRecordStrategy", "NONE").toUpperCase();

    }
    public Executor getExecutor() {
        return this.executor;
    }

    public String transactionId() {
        return coordinator.getMeta().transactionId();
    }

    public void setStatus(SprayStepStatus status) {
        this.stepStatus = status;
    }

    public void setError(Throwable error) {
        this.errorList.add(Pair.of(System.currentTimeMillis(), error));
    }

    public void recordBeforeExecute(SprayProcessStepExecutor fromExecutor, SprayStepStatus status, SprayData data) {

    }
    public void recordInputAgain(SprayStepStatus status, SprayData data) {

    }

    public long getStartTime() {

    }
    public long duration() {

    }

    public Map<String, Long> inputInfos() {

    }

    public Map<String, Long> outputInfos() {

    }
    public SprayStepStatus getStatus() {

    }

}
