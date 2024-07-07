package top.spray.engine.prop;


import top.spray.core.engine.props.SprayData;
import top.spray.engine.step.executor.SprayProcessStepExecutor;

public class SprayExecuteData {
    public static SprayExecuteData of(SprayProcessStepExecutor fromExecutor, SprayData data) {
        return new SprayExecuteData(fromExecutor.getCoordinator()
                .getExecutorNameKey(fromExecutor.getMeta()), data);
    }
    private final String dataKey;
    private final SprayData data;

    private SprayExecuteData(String dataKey, SprayData data) {
        this.dataKey = dataKey;
        this.data = data.unmodifiable();
    }

    public String getDataKey() {
        return dataKey;
    }

    public SprayData getData() {
        return data;
    }
}
