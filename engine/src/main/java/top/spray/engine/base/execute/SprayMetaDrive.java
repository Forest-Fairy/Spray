package top.spray.engine.base.execute;

import top.spray.engine.base.meta.SprayBaseMeta;

public interface SprayMetaDrive<T extends SprayBaseMeta<T>> {
    T getMeta();
}
