package top.spray.engine.coordinate.meta;

import top.spray.core.engine.meta.SprayBaseMeta;
import top.spray.core.engine.props.SprayData;
import top.spray.engine.step.meta.SprayProcessStepMeta;

import java.util.List;

public interface SprayProcessCoordinatorMeta extends SprayBaseMeta<SprayProcessCoordinatorMeta> {
    String getId();
    String getName();
    String transactionId();
    boolean asyncSupport();
    boolean remoteSupport();
    /** the url for remote executor to call, it will be auto replaced if blank when it is in-need */
    String url();

    String dataDispatchResultHandler();

    /** the default variables for the process */
    SprayData getDefaultProcessData();
    /** the startNodes for the coordinator */
    List<SprayProcessStepMeta> getStartNodes();

    List<SprayData> getDefaultDataList();

    String getGeneratorClass();
}
