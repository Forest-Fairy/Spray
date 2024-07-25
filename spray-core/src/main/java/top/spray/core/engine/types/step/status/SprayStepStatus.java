package top.spray.core.engine.types.step.status;

import top.spray.core.engine.types.SprayType;
import top.spray.core.engine.types.step.SprayStepDescription_i18n;
import top.spray.core.engine.types.step.SprayStepTypeName_i18n;
import top.spray.core.i18n.Spray_i18n;

public class SprayStepStatus implements SprayType {
    /** pause by config (debug or collecting data) */
    public static final SprayStepStatus PAUSED = new SprayStepStatus(
            3, "step.status.execution_paused");

    /** stop by human. need stop reason */
    public static final SprayStepStatus STOP = new SprayStepStatus(
            2, "step.status.manual_stop");

    /** stop normally. need status message */
    public static final SprayStepStatus DONE = new SprayStepStatus(
            1, "step.status.execution_done");

    /** running by default */
    public static final SprayStepStatus RUNNING = new SprayStepStatus(
            0, "step.status.execution_running");

    /** stop with exception. need fail message */
    public static final SprayStepStatus FAILED = new SprayStepStatus(
            -1, "step.status.execution_failed");

    /** stop for the accident occur, such as the server shut down. no error msg */
    public static final SprayStepStatus ERROR = new SprayStepStatus(
            -2, "step.status.error_stop");

    private final int statusCode;
    private final String i18n;
    SprayStepStatus(int statusCode, String i18n) {
        this.statusCode = statusCode;
        this.i18n = i18n;
    }

    @Override
    public int getCode() {
        return this.statusCode;
    }

    @Override
    public String typeName() {
        return Spray_i18n.get(
                SprayStepTypeName_i18n.class, this.i18n);
    }

    @Override
    public String getDescribeMsg() {
        return Spray_i18n.get(
                SprayStepDescription_i18n.class, this.i18n);
    }

    @Override
    public boolean isSameClass(Class<? extends SprayType> clazz) {
        return SprayStepStatus.class.isAssignableFrom(clazz);
    }

    @Override
    public boolean equals(Object obj) {
        return SprayType.equal(this, obj);
    }
}
