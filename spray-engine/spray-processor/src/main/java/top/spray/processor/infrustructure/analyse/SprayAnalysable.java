package top.spray.processor.infrustructure.analyse;

import java.util.List;
import java.util.Set;

public interface SprayAnalysable {
    <Result extends SprayAnalyseResult> List<Result> listAnalysed(String analyserName);
    <Result extends SprayAnalyseResult, Analyser extends SprayAnalyser<Result, ?>> List<Result> listAnalysed(Analyser analyser);
}
