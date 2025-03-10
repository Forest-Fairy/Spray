package top.spray.engine.process.infrastructure.meta;

/**
 * Define the base meta of a spray object
 */
public interface SprayBaseMeta extends Cloneable {
    /**
     * meta id storage in db
     */
    String getId();

    /**
     * meta name storage in db
     */
    String getName();

}
