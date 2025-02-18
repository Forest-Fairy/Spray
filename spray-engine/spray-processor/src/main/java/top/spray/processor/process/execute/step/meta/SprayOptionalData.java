package top.spray.processor.process.execute.step.meta;

import top.spray.core.global.prop.SprayData;
import top.spray.common.tools.tuple.SprayTuples;
import top.spray.common.tools.SprayOptional;

public class SprayOptionalData extends SprayOptional<SprayTuples._3<String, SprayData, Boolean>> {
    private final String dataKey;
    public SprayOptionalData() {
        this(null);
    }
    public SprayOptionalData(String generatorExecutorNameKey, SprayData data, boolean still) {
        this(new SprayTuples._3<>(generatorExecutorNameKey, data, still));
    }
    private SprayOptionalData(SprayTuples._3<String, SprayData, Boolean> value) {
        super(value);
        this.dataKey = this.isNotPresent() ? "" :
                String.format("%s@%s(%s)", this.isStill() ? "one" : "last",
                        this.getGeneratorNameKey(), this.getData().toJson());
    }
    public String getDataKey() {
        return dataKey;
    }
    public String getGeneratorNameKey() {
        return this.isNotPresent() ? "" : this.get().t0();
    }
    public SprayData getData() {
        return this.isNotPresent() ? SprayData.EMPTY : this.get().t1();
    }
    public boolean isStill() {
        return this.isPresent() && this.get().t2();
    }

    public SprayOptionalData copyIfPresent() {
        return this.isNotPresent() ? this : new SprayOptionalData(this.get().t0(), SprayData.deepCopy(this.get().t1()), this.get().t2());
    }
}
