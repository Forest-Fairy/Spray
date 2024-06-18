package top.spray.core.engine.execute;

public enum SprayStepActiveType {
    /** do execute this step */
    ACTIVE(2, "do execute this step"),
    /** don't execute this step but to execute its next nodes */
    IGNORE(1, "don't execute this step but to execute its next nodes"),
    /** don't execute this step and its next nodes  */
    INACTIVE(0, "don't execute this step and its next nodes"),
    ;
    private final int code;
    private final String state;

    SprayStepActiveType(int code, String state) {
        this.code = code;
        this.state = state;
    }
}
