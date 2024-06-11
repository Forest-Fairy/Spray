package top.spray.core.engine.execute;

import top.spray.core.engine.meta.SprayBaseMeta;

public interface SprayMetaDrive<T extends SprayBaseMeta<T>> {
    T getMeta();
}
