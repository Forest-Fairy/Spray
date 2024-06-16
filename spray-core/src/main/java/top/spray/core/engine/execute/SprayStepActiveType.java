package top.spray.core.engine.execute;

public enum SprayStepActiveType {
    /** do execute this step */
    ACTIVE,
    /** don't execute this step but to execute its next nodes */
    IGNORE,
    /** don't execute this step and its next nodes  */
    INACTIVE,
    ;
}
