package top.spray.processor.process.data.analyse.type;

import top.spray.processor.process.data.analyse.direction.SprayDataDirection;
import top.spray.processor.process.execute.step.executor.facade.SprayStepFacade;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SprayFileTypeAnalyser extends SprayAbstractDataTypeAnalyser {
    public static final SprayFileTypeAnalyser INSTANCE = new SprayFileTypeAnalyser();
    // filePath
    private SprayFileTypeAnalyser() {
        super("FILE",  "serverId", "filePath");
    }

    @Override
    protected Map<String, Object> doAnalyse(SprayStepFacade executorFacade, SprayDataDirection direction, Map<String, Object> paramMap) {
        String serverId = Objects.toString(paramMap.get("serverId"), "");
        String filePath = Objects.toString(paramMap.get("filePath"), "");
        String type = Objects.toString(paramMap.get("fileType"), "");
        String name = Objects.toString(paramMap.get("fileName"), "");
        String size = Objects.toString(paramMap.get("fileSize"), "");
        if (serverId.isEmpty() || filePath.isEmpty()) {
            return lackOfParam("serverId", "filePath");
        }
        Map<String, Object> info = new HashMap<>();
        info.put("serverId", serverId);
        info.put("filePath", filePath);
        if (!type.trim().isEmpty()) {
            info.put("fileType", type);
        }
        if (!name.trim().isEmpty()) {
            info.put("fileName", name);
        }
        if (!size.trim().isEmpty()) {
            info.put("fileSize", size);
        }
        return Map.copyOf(info);
    }
}
