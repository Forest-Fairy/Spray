package top.spray.engine.coordinate.meta;

import top.spray.engine.base.meta.SprayBaseMeta;
import top.spray.engine.step.meta.SprayProcessStepMeta;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public interface SprayProcessCoordinatorMeta extends SprayBaseMeta<SprayProcessCoordinatorMeta> {
    String getId();
    String getName();
    String transactionId();

    /** the default variables for the process */
    Map<String, Object> getDefaultProcessData();
    /** the startNodes for the coordinator */
    List<? extends SprayProcessStepMeta> getStartNodes();

    int minThreadCount();

    @Override
    default String listJarFiles() {
        return this.getStartNodes().stream()
                .map(SprayBaseMeta::listJarFiles)
                .filter(String::isBlank)
                .collect(Collectors.joining(","));
    }
}
