package top.spray.engine.base.execute;

import top.spray.engine.base.meta.SprayBaseMeta;
import top.spray.engine.step.meta.SprayProcessStepMeta;

public interface SprayMetaDrive<T extends SprayBaseMeta<T>> {
    T getMeta();
}
