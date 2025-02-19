package top.spray.engine.process.processor.data.event.impl;

import top.spray.core.type.SprayType;
import top.spray.engine.process.processor.dispatch.i18n.SprayCoordinatorType;

import java.util.List;

public class SprayDataDispatchResultType implements SprayCoordinatorType {
    private static final List<SprayDataDispatchResultType> values = List.of(
            SprayDataDispatchResultType.SUCCESS,
            SprayDataDispatchResultType.ABANDONED,
            SprayDataDispatchResultType.SKIPPED,
            SprayDataDispatchResultType.FILTERED,
            SprayDataDispatchResultType.FAILED,
            SprayDataDispatchResultType.ERRORED
    );
    public static List<SprayDataDispatchResultType> values() {
        return values;
    }
    public static SprayDataDispatchResultType get(int code) {
        return SprayType.get(values, code);
    }


    /** successfully execute with step */
    public static final SprayDataDispatchResultType SUCCESS = new SprayDataDispatchResultType(
            1, "dispatch.result.status.success");

    /** the data is abandoned because of no next step or next step does not want to execute */
    public static final SprayDataDispatchResultType ABANDONED = new SprayDataDispatchResultType(
            0, "dispatch.result.status.abandoned");

    /** skipped by node config */
    public static final SprayDataDispatchResultType SKIPPED = new SprayDataDispatchResultType(
            -1, "dispatch.result.status.skipped");

    /** filtered by filter */
    public static final SprayDataDispatchResultType FILTERED = new SprayDataDispatchResultType(
            -2, "dispatch.result.status.filtered");

    /** exception occur. need fail message */
    public static final SprayDataDispatchResultType FAILED = new SprayDataDispatchResultType(
            -3, "dispatch.result.status.failed");

    /** the transaction stop but data status is not SUCCESS or FAILED */
    public static final SprayDataDispatchResultType ERRORED = new SprayDataDispatchResultType(
            -4, "dispatch.result.status.errored");

    private final int code;
    private final String i18n;
    SprayDataDispatchResultType(int code, String i18n) {
        this.code = code;
        this.i18n = i18n;
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
}
