package top.spray.processor.infrustructure.meta;


public interface SprayMetaDrive<T extends SprayBaseMeta> {
    T getMeta();
    String transactionId();
}
