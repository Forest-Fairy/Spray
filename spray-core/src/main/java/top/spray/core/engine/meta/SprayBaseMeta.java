package top.spray.core.engine.meta;


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

}
