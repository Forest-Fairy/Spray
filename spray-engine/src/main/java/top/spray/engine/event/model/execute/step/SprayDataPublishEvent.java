package top.spray.engine.event.model.execute.step;

import top.spray.core.engine.props.SprayData;
import top.spray.engine.event.model.SprayBaseEvent;
import top.spray.engine.event.model.execute.SprayExecuteEvent;
import top.spray.engine.prop.SprayVariableContainer;
import top.spray.engine.step.executor.SprayExecutorDefinition;

public class SprayDataPublishEvent extends SprayBaseEvent implements SprayExecuteEvent {
    public static final String NAME = "data_publish";
    private final SprayVariableContainer variableContainer;
    private final SprayExecutorDefinition fromExecutorDefinition;
    private final SprayData data;
    private final boolean still;

    public SprayDataPublishEvent(SprayVariableContainer variableContainer,
                                 SprayExecutorDefinition fromExecutorDefinition, SprayData data, boolean still, long createTime) {
        super(fromExecutorDefinition.getCoordinator(), NAME, createTime);
        this.variableContainer = variableContainer;
        this.fromExecutorDefinition = fromExecutorDefinition;
        this.data = data;
        this.still = still;
    }

    @Override
    public SprayExecutorDefinition getDefinition() {
        return this.fromExecutorDefinition;
    }

    public SprayVariableContainer getVariableContainer() {
        return variableContainer;
    }

    public SprayExecutorDefinition getFromExecutorDefinition() {
        return fromExecutorDefinition;
    }

    public SprayData getData() {
        return data;
    }

    public boolean isStill() {
        return still;
    }
}
