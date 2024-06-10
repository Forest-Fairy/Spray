package top.spray.engine.step.meta;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.spray.engine.base.meta.SprayBaseMeta;
import top.spray.engine.coordinate.meta.SprayProcessCoordinatorMeta;
import top.spray.engine.step.executor.SprayProcessStepExecutor;

import java.util.List;

/**
 * 节点引擎
 */
public interface SprayProcessStepMeta extends SprayBaseMeta<SprayProcessStepMeta> {
    String getId();
    String getName();

    List<SprayProcessStepMeta> nextNodes();

    boolean isTransactional();

    boolean rollbackIfError();

    boolean ignoreError();

    Class<? extends SprayProcessStepExecutor> executorClass();


}
