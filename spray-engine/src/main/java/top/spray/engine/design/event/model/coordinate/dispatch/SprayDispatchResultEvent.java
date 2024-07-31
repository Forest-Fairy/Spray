package top.spray.engine.design.event.model.coordinate.dispatch;

import top.spray.core.engine.props.SprayData;
import top.spray.core.engine.types.data.dispatch.result.SprayDataDispatchResultStatus;
import top.spray.engine.coordinate.coordinator.SprayProcessCoordinator;
import top.spray.engine.design.event.model.coordinate.SprayCoordinateEvent;
import top.spray.engine.step.meta.SprayProcessStepMeta;

public class SprayDispatchResultEvent extends SprayCoordinateEvent {
    public static final String NAME = "dispatch_result";
    private final String variablesIdentityDataKey;
    private final SprayProcessStepMeta fromExecutorMeta;
    private final SprayData data;
    private final boolean still;
    private final SprayProcessStepMeta nextMeta;
    private final SprayDataDispatchResultStatus dataDispatchStatus;

    public SprayDispatchResultEvent(long eventTime, SprayProcessCoordinator coordinator, String variablesIdentityDataKey,
                                    SprayProcessStepMeta fromExecutorMeta, SprayData data, boolean still,
                                    SprayProcessStepMeta nextMeta, SprayDataDispatchResultStatus dataDispatchStatus) {
        super(coordinator, NAME, eventTime);
        this.variablesIdentityDataKey = variablesIdentityDataKey;
        this.fromExecutorMeta = fromExecutorMeta;
        this.data = data;
        this.still = still;
        this.nextMeta = nextMeta;
        this.dataDispatchStatus = dataDispatchStatus;
    }

    public String getVariablesIdentityDataKey() {
        return variablesIdentityDataKey;
    }

    public SprayProcessStepMeta getFromExecutorMeta() {
        return fromExecutorMeta;
    }

    public SprayData getData() {
        return data;
    }

    public boolean isStill() {
        return still;
    }

    public SprayProcessStepMeta getNextMeta() {
        return nextMeta;
    }

    public SprayDataDispatchResultStatus getDataDispatchStatus() {
        return dataDispatchStatus;
    }
}
