package top.spray.engine.process.processor.execute.step.status;

import top.spray.core.type.SprayType;
import top.spray.engine.process.processor.execute.i18n.SprayStepExecuteType;

import java.util.List;

public class SprayStepStatus implements SprayStepExecuteType {
    private static final List<SprayStepStatus> values = List.of(
            SprayStepStatus.WAITING,
            SprayStepStatus.SUSPENDING,
            SprayStepStatus.STOP,
            SprayStepStatus.DONE,
            SprayStepStatus.FAILED,
            SprayStepStatus.ERROR
    );
    public static List<SprayStepStatus> values() {
        return values;
    }
    public static SprayStepStatus get(int code) {
        return SprayType.get(values, code);
    }

    /** running by default */
    public static final SprayStepStatus WAITING = new SprayStepStatus(
            0, "step_status.execution_waiting");

    /** running by default */
    public static final SprayStepStatus PROCESSING = new SprayStepStatus(
            0, "step_status.execution_processing");

    /** suspended by config (debugged or collecting data) */
    public static final SprayStepStatus SUSPENDING = new SprayStepStatus(
            3, "step_status.execution_suspending");

    /** stopped by human. need stop reason */
    public static final SprayStepStatus STOP = new SprayStepStatus(
            2, "step_status.manual_stop");

    /** stop normally. need status message */
    public static final SprayStepStatus DONE = new SprayStepStatus(
            1, "step_status.execution_done");


    /** stop with exception. need fail message */
    public static final SprayStepStatus FAILED = new SprayStepStatus(
            -1, "step_status.execution_failed");

    /** stop for the accident occur, such as the server shut down. no error msg */
    public static final SprayStepStatus ERROR = new SprayStepStatus(
            -2, "step_status.error_stop");

    private final int statusCode;
    private final String i18n;
    SprayStepStatus(int statusCode, String i18n) {
        this.statusCode = statusCode;
        this.i18n = i18n;
    }

    @Override
    public boolean equals(Object obj) {
        return SprayType.isEqual(this, obj);
    }

    @Override
    public String i18nKey() {
        return this.i18n;
    }

    @Override
    public int getCode() {
        return this.statusCode;
    }

}
