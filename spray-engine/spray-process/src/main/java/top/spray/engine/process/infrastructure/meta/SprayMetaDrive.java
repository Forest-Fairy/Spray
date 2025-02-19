package top.spray.engine.process.infrastructure.meta;


public interface SprayMetaDrive<T extends SprayBaseMeta> {
    T getMeta();
    String transactionId();
}
