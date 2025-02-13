package top.spray.processor.infrustructure.analyse;

public interface SprayAnalyser<Result extends SprayAnalyseResult, Analysable extends SprayAnalysable> {

    String analyserName();

    boolean isSupport(SprayAnalysable analysable);

    /**
     * do analyse
     */
    Result analyse(Analysable analysable, Object... params);

}
