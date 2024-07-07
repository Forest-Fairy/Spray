package top.spray.engine.prop;

import top.spray.core.engine.props.SprayData;
import top.spray.engine.coordinate.coordinator.SprayProcessCoordinator;
import top.spray.engine.step.executor.SprayProcessStepExecutor;

public class SprayVariableContainer {
    private final String creator;
    private final long createTime;
    private final SprayData data;
    private final String key;

    private SprayVariableContainer(String creator, long createTime, SprayData data, String key) {
        this.creator = creator;
        this.createTime = createTime;
        this.data = data;
        this.key = key;
    }

    public String creator() {
        return creator;
    }

    public long createTime() {
        return createTime;
    }

    public SprayData data() {
        return data;
    }

    public String key() {
        return key;
    }

    public String nextKey(SprayProcessStepExecutor executor) {
        return this.key + "_" + creator;
    }

    public static SprayVariableContainer create(SprayProcessCoordinator coordinator) {
        String creator = coordinator.getMeta().getName() + "[" + coordinator.getMeta().transactionId() + "]";
        long createTime = System.currentTimeMillis();
        SprayData data = SprayData.deepCopy(coordinator.getMeta().getDefaultProcessData());
        String key = createTime + "#" + creator;
        return new SprayVariableContainer(creator, createTime, data, key);
    }
    public static SprayVariableContainer easyCopy(SprayVariableContainer last, SprayProcessStepExecutor executor) {
        return new SprayVariableContainer(
                executor.getCoordinator().getExecutorNameKey(executor.getMeta()),
                System.currentTimeMillis(),
                new SprayData(last.data),
                last.nextKey(executor));
    }
    public static SprayVariableContainer deepCopy(SprayVariableContainer last, SprayProcessStepExecutor executor) {
        return new SprayVariableContainer(
                executor.getCoordinator().getExecutorNameKey(executor.getMeta()),
                System.currentTimeMillis(),
                SprayData.deepCopy(last.data),
                last.nextKey(executor));
    }
}
