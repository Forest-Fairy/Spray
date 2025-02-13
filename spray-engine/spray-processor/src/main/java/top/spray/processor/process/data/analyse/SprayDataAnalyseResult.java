package top.spray.processor.process.data.analyse;

import top.spray.processor.infrustructure.analyse.SprayAnalyseResult;

public interface SprayDataAnalyseResult extends SprayAnalyseResult {
    String dataId();
    String direction();
}
