package top.spray.engine.process.infrastructure.analyse;

import java.util.Map;

public interface SprayAnalyseResult {
    String analyserName();
    String resultId();
    Map<String, Object> info();
}
