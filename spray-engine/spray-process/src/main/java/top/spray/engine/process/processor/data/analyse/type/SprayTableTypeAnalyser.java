package top.spray.engine.process.processor.data.analyse.type;

import top.spray.engine.process.processor.data.analyse.direction.SprayDataDirection;
import top.spray.engine.process.processor.execute.step.executor.facade.SprayStepFacade;

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
