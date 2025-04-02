package top.spray.engine.process.processor.execute.step.meta;

import top.spray.common.data.SprayData;
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
        return this.isNotPresent() ? "" : this.map(SprayTuples._2::t1).orElse(null);
    }
    public SprayData getData() {
        return this.isNotPresent() ? SprayData.EMPTY : this.map(SprayTuples._2::t2).orElse(null);
    }
    public boolean isStill() {
        return this.isPresent() && this.map(SprayTuples._3::t3).orElse(null);
    }

    public SprayOptionalData copyIfPresent() {
        return this.isNotPresent() ? this : new SprayOptionalData(this.map(SprayTuples._2::t1).orElse(null), SprayData.deepCopy(this.map(SprayTuples._2::t2).orElse(null)), this.map(SprayTuples._3::t3).orElse(null));
    }
}
