package top.spray.engine.base.meta;

import org.slf4j.Logger;

/**
 * Define the base meta of a spray object
 */
public interface SprayBaseMeta<T extends SprayBaseMeta<?>> extends Cloneable {
    /**
     * meta id storage in db
     */
    String getId();

    /**
     * meta name storage in db
     */
    String getName();

    /**
     * current transaction id
     */
    String transactionId();

    Logger logger();

    /**
     * 列出节点需要的jar文件
     */
    String listJarFiles();

}
