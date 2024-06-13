package top.spray.core.engine.meta;

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

    /** run this step async */
    boolean isAsync();

}
