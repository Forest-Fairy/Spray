package top.spray.core.engine.types.coordinate.status;

import top.spray.core.engine.types.SprayType;
import top.spray.core.engine.types.coordinate.SprayTypeCoordinateDescription_i18n;
import top.spray.core.engine.types.coordinate.SprayTypeCoordinateTypeName_i18n;
import top.spray.core.i18n.Spray_i18n;

public class SprayCoordinatorStatus implements SprayType {
    /** stop by human. need stop reason */
    public static final SprayCoordinatorStatus STOP = new SprayCoordinatorStatus(
            2, "coordinator.status.manual_stop");

    /** stop normally. need status message */
    public static final SprayCoordinatorStatus SUCCESS = new SprayCoordinatorStatus(
            1, "coordinator.status.execution_success");
    /** running by default */
    public static final SprayCoordinatorStatus RUNNING = new SprayCoordinatorStatus(
            0, "coordinator.status.execution_running");
    /** stop with exception. need fail message */
    public static final SprayCoordinatorStatus FAILED = new SprayCoordinatorStatus(
            -1, "coordinator.status.execution_failed");
    /** stop for the accident occur, such as the server shut down. no error msg */
    public static final SprayCoordinatorStatus ERROR = new SprayCoordinatorStatus(
            -2, "coordinator.status.error_stop");

    private final int code;
    private final String i18n;
    SprayCoordinatorStatus(int code, String i18n) {
        this.code = code;
        this.i18n = i18n;
    }

    @Override
    public int getCode() {
        return this.code;
    }

    @Override
    public String typeName() {
        return Spray_i18n.get(
                SprayTypeCoordinateTypeName_i18n.class, this.i18n);
    }

    @Override
    public String getDescribeMsg() {
        return Spray_i18n.get(
                SprayTypeCoordinateDescription_i18n.class, this.i18n);
    }

    @Override
    public boolean isSameClass(Class<? extends SprayType> clazz) {
        return SprayCoordinatorStatus.class.isAssignableFrom(clazz);
    }

    @Override
    public boolean equals(Object obj) {
        return SprayType.equal(this, obj);
    }
}
