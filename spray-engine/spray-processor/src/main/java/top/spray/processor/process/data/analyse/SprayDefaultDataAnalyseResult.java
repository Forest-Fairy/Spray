package top.spray.processor.process.data.analyse;

import top.spray.common.tools.Sprays;
import top.spray.processor.process.data.analyse.direction.SprayDataDirection;

import java.util.Collections;
import java.util.Map;

public class SprayDefaultDataAnalyseResult implements SprayDataAnalyseResult {
    private String dataId;
    private String analyserName;
    private String resultId;
    private Map<String, Object> info;
    private String direction;

    public SprayDefaultDataAnalyseResult() {
    }

    public SprayDefaultDataAnalyseResult(SprayDataAnalyser<?> analyser,
                                         String dataId,
                                         SprayDataDirection direction,
                                         Map<String, Object> info) {
        this.resultId = Sprays.UUID();
        this.dataId = dataId;
        this.analyserName = analyser.analyserName();
        this.direction = direction.getName();
        this.info = info;
    }

    @Override
    public String dataId() {
        return this.dataId;
    }

    @Override
    public String direction() {
        return this.direction;
    }

    @Override
    public String analyserName() {
        return this.analyserName;
    }

    @Override
    public String resultId() {
        return this.resultId;
    }

    @Override
    public Map<String, Object> info() {
        return Collections.unmodifiableMap(this.info);
    }

    public Map<String, Object> getInfo() {
        return info;
    }
}
