package top.spray.engine.process.infrastructure.analyse;

import java.util.List;

public interface SprayAnalysable {
    <Result extends SprayAnalyseResult> List<Result> listAnalysed(String analyserName);
    <Result extends SprayAnalyseResult, Analyser extends SprayAnalyser<Result, ?>> List<Result> listAnalysed(Analyser analyser);
}
