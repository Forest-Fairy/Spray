package top.spray.processor.process.dispatch.coordinate.status;

import top.spray.core.system.type.SprayType;
import top.spray.core.system.type.SprayTypeI18nType;

import java.util.List;

public class SprayCoordinatorStatus implements SprayTypeI18nType {
    private static final List<SprayCoordinatorStatus> values = List.of(
            SprayCoordinatorStatus.RUNNING,
            SprayCoordinatorStatus.STOP,
            SprayCoordinatorStatus.FINISH,
            SprayCoordinatorStatus.FAILED,
            SprayCoordinatorStatus.ERROR
    );
    public static List<SprayCoordinatorStatus> values() {
        return values;
    }
    public static SprayCoordinatorStatus get(int code) {
        return SprayType.get(values, code);
    }

    /** initializing, set by default */
    public static final SprayCoordinatorStatus INITIALIZING = new SprayCoordinatorStatus(
            0, "coordinator.status.initializing", false);
    /** running, witch can start data coordinator */
    public static final SprayCoordinatorStatus RUNNING = new SprayCoordinatorStatus(
            0, "coordinator.status.running", false);

    /** stop normally. need status message */
    public static final SprayCoordinatorStatus FINISH = new SprayCoordinatorStatus(
            1, "coordinator.status.finish", true);

    /** stop by human. need stop reason */
    public static final SprayCoordinatorStatus STOP = new SprayCoordinatorStatus(
            2, "coordinator.status.aborted", true);

    /** stop with exception. need fail message */
    public static final SprayCoordinatorStatus FAILED = new SprayCoordinatorStatus(
            -1, "coordinator.status.failed", true);

    /** stop for the accident occurred, such as the server shut down. no error msg */
    public static final SprayCoordinatorStatus ERROR = new SprayCoordinatorStatus(
            -2, "coordinator.status.error", true);

    private final int code;
    private final String i18n;
    private final boolean isEnd;
    SprayCoordinatorStatus(int code, String i18n, boolean isEnd) {
        this.code = code;
        this.i18n = i18n;
        this.isEnd = isEnd;
    }

    @Override
    public int getCode() {
        return this.code;
    }

    @Override
    public String i18nKey() {
        return this.i18n;
    }

    @Override
    public boolean equals(Object obj) {
        return SprayType.isEqual(this, obj);
    }

    public boolean isEnd() {
        return isEnd;
    }

}
