package top.spray.engine.step.meta;

import top.spray.core.engine.meta.SprayBaseMeta;
import top.spray.engine.step.executor.SprayProcessStepExecutor;

import java.util.Date;
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

    String get(String key);
    Integer getInteger(String key, Integer defVal);
    Boolean getBoolean(String key, Boolean bool);
    Date getDate(String key, Date defVal);

}
