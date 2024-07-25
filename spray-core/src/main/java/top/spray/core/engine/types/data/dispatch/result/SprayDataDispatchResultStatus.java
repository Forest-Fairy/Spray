package top.spray.core.engine.types.data.dispatch.result;

import top.spray.core.engine.types.SprayType;
import top.spray.core.engine.types.coordinate.SprayTypeCoordinateTypeName_i18n;
import top.spray.core.engine.types.data.dispatch.SprayDataDispatchDescription_i18n;
import top.spray.core.engine.types.data.dispatch.SprayDataDispatchTypeName_i18n;
import top.spray.core.i18n.Spray_i18n;

import java.util.List;

public class SprayDataDispatchResultStatus implements SprayType {
    private static final List<SprayDataDispatchResultStatus> values = List.of(
            SprayDataDispatchResultStatus.SUCCESS,
            SprayDataDispatchResultStatus.ABANDONED,
            SprayDataDispatchResultStatus.SKIPPED,
            SprayDataDispatchResultStatus.FILTERED,
            SprayDataDispatchResultStatus.FAILED,
            SprayDataDispatchResultStatus.ERRORED
    );
    public static List<SprayDataDispatchResultStatus> values() {
        return values;
    }
    public static SprayDataDispatchResultStatus get(int code) {
        return SprayType.get(SprayDataDispatchResultStatus.values(), code);
    }


    /** successfully execute with step */
    public static final SprayDataDispatchResultStatus SUCCESS = new SprayDataDispatchResultStatus(
            1, "result.status.success");


    /** the data is abandoned because there is no next step for executing */
    public static final SprayDataDispatchResultStatus ABANDONED = new SprayDataDispatchResultStatus(
            0, "result.status.abandoned");


    /** skipped by node config */
    public static final SprayDataDispatchResultStatus SKIPPED = new SprayDataDispatchResultStatus(
            -1, "result.status.skipped");


    /** filtered by filter */
    public static final SprayDataDispatchResultStatus FILTERED = new SprayDataDispatchResultStatus(
            -2, "result.status.filtered");


    /** exception occur. need fail message */
    public static final SprayDataDispatchResultStatus FAILED = new SprayDataDispatchResultStatus(
            -3, "result.status.failed");


    /** the transaction stop but data status is not SUCCESS or FAILED */
    public static final SprayDataDispatchResultStatus ERRORED = new SprayDataDispatchResultStatus(
            -4, "result.status.errored");

    private final int code;
    private final String i18n;
    SprayDataDispatchResultStatus(int code, String i18n) {
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
                SprayDataDispatchTypeName_i18n.class, this.i18n);
    }

    @Override
    public String getDescribeMsg(){
        return Spray_i18n.get(
                SprayDataDispatchDescription_i18n.class, this.i18n);
    }

    @Override
    public boolean isSameClass(Class<? extends SprayType> clazz) {
        return SprayDataDispatchResultStatus.class.isAssignableFrom(clazz);
    }

    @Override
    public boolean equals(Object obj) {
        return SprayType.equal(this, obj);
    }
}
