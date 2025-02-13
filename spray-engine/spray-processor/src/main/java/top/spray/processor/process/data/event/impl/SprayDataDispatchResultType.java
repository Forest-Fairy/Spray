package top.spray.processor.process.data.event.impl;

import top.spray.core.i18n.Spray_i18nBundleDef;
import top.spray.core.system.type.SprayType;
import top.spray.core.system.type.SprayTypeI18nType;
import top.spray.processor.process.dispatch.i18n.SprayDispatchDescription_i18n;
import top.spray.processor.process.dispatch.i18n.SprayDispatchTypeName_i18n;

import java.util.List;

public class SprayDataDispatchResultType implements SprayTypeI18nType {
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
            1, "result.status.success");


    /** the data is abandoned because there is no next step for executing */
    public static final SprayDataDispatchResultType ABANDONED = new SprayDataDispatchResultType(
            0, "result.status.abandoned");


    /** skipped by node config */
    public static final SprayDataDispatchResultType SKIPPED = new SprayDataDispatchResultType(
            -1, "result.status.skipped");


    /** filtered by filter */
    public static final SprayDataDispatchResultType FILTERED = new SprayDataDispatchResultType(
            -2, "result.status.filtered");


    /** exception occur. need fail message */
    public static final SprayDataDispatchResultType FAILED = new SprayDataDispatchResultType(
            -3, "result.status.failed");


    /** the transaction stop but data status is not SUCCESS or FAILED */
    public static final SprayDataDispatchResultType ERRORED = new SprayDataDispatchResultType(
            -4, "result.status.errored");

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
    public Class<? extends Spray_i18nBundleDef> i18nClass(TargetType target) {
        return switch (target) {
            case NAME -> SprayDispatchTypeName_i18n.class;
            case DESCRIPTION -> SprayDispatchDescription_i18n.class;
            default -> unsupported(target);
        };
    }


    @Override
    public boolean equals(Object obj) {
        return SprayType.isEqual(this, obj);
    }
}
