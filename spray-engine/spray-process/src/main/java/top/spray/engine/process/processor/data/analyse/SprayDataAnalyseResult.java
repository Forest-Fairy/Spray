package top.spray.engine.process.processor.data.analyse;

import top.spray.engine.process.infrastructure.analyse.SprayAnalyseResult;

public interface SprayDataAnalyseResult extends SprayAnalyseResult {
    String dataId();
    String direction();
}
