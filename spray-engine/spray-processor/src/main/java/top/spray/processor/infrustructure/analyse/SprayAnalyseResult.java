package top.spray.processor.infrustructure.analyse;

import java.util.Map;

public interface SprayAnalyseResult {
    String analyserName();
    String resultId();
    Map<String, Object> info();
}
