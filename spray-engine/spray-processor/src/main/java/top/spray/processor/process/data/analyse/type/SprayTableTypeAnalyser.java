package top.spray.processor.process.data.analyse.type;

import top.spray.processor.process.data.analyse.direction.SprayDataDirection;
import top.spray.processor.process.execute.step.executor.facade.SprayStepFacade;

import java.util.Map;

public class SprayTableTypeAnalyser extends SprayAbstractDataTypeAnalyser {
    public static final SprayTableTypeAnalyser INSTANCE = new SprayTableTypeAnalyser();
    private SprayTableTypeAnalyser() {
        super("TABLE",  "tableName");
    }

    @Override
    protected Map<String, Object> doAnalyse(SprayStepFacade executorFacade, SprayDataDirection direction, Map<String, Object> paramMap) {
        return Map.of();
    }
}
