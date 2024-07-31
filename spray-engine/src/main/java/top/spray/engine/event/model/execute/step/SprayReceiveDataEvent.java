package top.spray.engine.event.model.execute.step;

import top.spray.core.engine.props.SprayData;
import top.spray.engine.coordinate.coordinator.SprayProcessCoordinator;
import top.spray.engine.event.model.SprayBaseEvent;
import top.spray.engine.prop.SprayVariableContainer;

public class SprayReceiveDataEvent extends SprayBaseEvent {
    public static final String NAME = "data_receive";
    private final SprayVariableContainer lastVariables;
    private final String fromExecutorNameKey;
    private final SprayData data;
    private final boolean still;

    public SprayReceiveDataEvent(SprayProcessCoordinator coordinator, SprayVariableContainer lastVariables, String fromExecutorNameKey, SprayData data, boolean still) {
        super(coordinator, NAME, System.currentTimeMillis());
        this.lastVariables = lastVariables;
        this.fromExecutorNameKey = fromExecutorNameKey;
        this.data = data;
        this.still = still;
    }

    public SprayVariableContainer getLastVariables() {
        return lastVariables;
    }

    public String getFromExecutorNameKey() {
        return fromExecutorNameKey;
    }

    public SprayData getData() {
        return data;
    }

    public boolean isStill() {
        return still;
    }
}