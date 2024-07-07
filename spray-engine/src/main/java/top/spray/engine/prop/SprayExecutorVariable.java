package top.spray.engine.prop;

import top.spray.core.engine.props.SprayData;
import top.spray.engine.coordinate.coordinator.SprayProcessCoordinator;
import top.spray.engine.step.executor.SprayProcessStepExecutor;

public class SprayExecutorVariable {
    private final String creator;
    private final long createTime;
    private final SprayData data;
    private final String key;

    private SprayExecutorVariable(String creator, long createTime, SprayData data, String key) {
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

    public static SprayExecutorVariable create(SprayProcessCoordinator coordinator) {
        String creator = coordinator.getMeta().getName() + "[" + coordinator.getMeta().transactionId() + "]";
        long createTime = System.currentTimeMillis();
        SprayData data = SprayData.deepCopy(coordinator.getMeta().getDefaultProcessData());
        String key = createTime + "#" + creator;
        return new SprayExecutorVariable(creator, createTime, data, key);
    }
    public static SprayExecutorVariable easyCopy(SprayExecutorVariable last, SprayProcessStepExecutor executor) {
        String creator = executor.getCoordinator().getExecutorNameKey(executor.getMeta());
        String newKey = last.nextKey(executor);
        return new SprayExecutorVariable(creator, System.currentTimeMillis(), new SprayData(last.data), newKey);
    }
    public static SprayExecutorVariable deepCopy(SprayExecutorVariable last, SprayProcessStepExecutor executor) {
        String creator = executor.getCoordinator().getExecutorNameKey(executor.getMeta());
        String newKey = last.nextKey(executor);
        return new SprayExecutorVariable(creator, System.currentTimeMillis(), SprayData.deepCopy(last.data), newKey);
    }
}
