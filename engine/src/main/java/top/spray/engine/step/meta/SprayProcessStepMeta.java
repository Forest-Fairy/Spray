package top.spray.engine.step.meta;

import top.spray.engine.base.meta.SprayBaseMeta;
import top.spray.engine.coordinate.meta.SprayProcessCoordinatorMeta;

/**
 * 节点引擎
 */
public interface SprayProcessStepMeta extends SprayBaseMeta<SprayProcessStepMeta> {
    String getId();
    String getName();

    SprayProcessCoordinatorMeta getProcessEngine();
    default String transactionId() {
        return this.getProcessEngine().transactionId();
    }

    /**
     * 是否事务操作
     */
    boolean isTransactional();

    /**
     * 回滚操作
     */
    void rollback();

    /**
     * 出错时是否回滚
     */
    boolean rollbackIfError();



}
