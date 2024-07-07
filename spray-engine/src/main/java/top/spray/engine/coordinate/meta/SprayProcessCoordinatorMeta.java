package top.spray.engine.coordinate.meta;

import top.spray.core.engine.meta.SprayBaseMeta;
import top.spray.core.engine.props.SprayData;
import top.spray.engine.step.meta.SprayProcessStepMeta;

import java.util.List;
import java.util.Map;

public interface SprayProcessCoordinatorMeta extends SprayBaseMeta<SprayProcessCoordinatorMeta> {
    String getId();
    String getName();
    String transactionId();
    int minThreadCount();
    boolean asyncSupport();

    /** the execute host if the process is remote task */
    String getCoordinatorHost();


    /** the default variables for the process */
    SprayData getDefaultProcessData();
    /** the startNodes for the coordinator */
    List<SprayProcessStepMeta> getStartNodes();

    List<SprayData> getDefaultDataList();
}
