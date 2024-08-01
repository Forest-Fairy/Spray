package top.spray.core.engine.types.data.dispatch.result;

import top.spray.core.engine.types.SprayType;
import top.spray.core.engine.types.data.dispatch.SprayDataDispatchDescription_i18n;
import top.spray.core.engine.types.data.dispatch.SprayDataDispatchTypeName_i18n;
import top.spray.core.i18n.Spray_i18n;

import java.util.List;

public class SprayEventDispatchResultStatus implements SprayType {
    private static final List<SprayEventDispatchResultStatus> values = List.of(
            SprayEventDispatchResultStatus.SUCCESS,
            SprayEventDispatchResultStatus.ABANDONED,
            SprayEventDispatchResultStatus.SKIPPED,
            SprayEventDispatchResultStatus.FILTERED,
            SprayEventDispatchResultStatus.FAILED,
            SprayEventDispatchResultStatus.ERRORED
    );
    public static List<SprayEventDispatchResultStatus> values() {
        return values;
    }
    public static SprayEventDispatchResultStatus get(int code) {
        return SprayType.get(values, code);
    }


    /** successfully execute with step */
    public static final SprayEventDispatchResultStatus SUCCESS = new SprayEventDispatchResultStatus(
            1, "result.status.success");


    /** the data is abandoned because there is no next step for executing */
    public static final SprayEventDispatchResultStatus ABANDONED = new SprayEventDispatchResultStatus(
            0, "result.status.abandoned");


    /** skipped by node config */
    public static final SprayEventDispatchResultStatus SKIPPED = new SprayEventDispatchResultStatus(
            -1, "result.status.skipped");


    /** filtered by filter */
    public static final SprayEventDispatchResultStatus FILTERED = new SprayEventDispatchResultStatus(
            -2, "result.status.filtered");


    /** exception occur. need fail message */
    public static final SprayEventDispatchResultStatus FAILED = new SprayEventDispatchResultStatus(
            -3, "result.status.failed");


    /** the transaction stop but data status is not SUCCESS or FAILED */
    public static final SprayEventDispatchResultStatus ERRORED = new SprayEventDispatchResultStatus(
            -4, "result.status.errored");

    private final int code;
    private final String i18n;
    SprayEventDispatchResultStatus(int code, String i18n) {
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
        return SprayEventDispatchResultStatus.class.isAssignableFrom(clazz);
    }

    @Override
    public boolean equals(Object obj) {
        return SprayType.isEqual(this, obj);
    }
}
