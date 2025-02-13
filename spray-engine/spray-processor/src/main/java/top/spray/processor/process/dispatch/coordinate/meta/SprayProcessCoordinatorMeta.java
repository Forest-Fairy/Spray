package top.spray.processor.process.dispatch.coordinate.meta;

import top.spray.core.global.prop.SprayData;
import top.spray.processor.infrustructure.meta.SprayBaseMeta;
import top.spray.processor.process.execute.step.meta.SprayProcessExecuteStepMeta;

import java.util.List;

public interface SprayProcessCoordinatorMeta extends SprayBaseMeta {
    @Override
    String getId();

    @Override
    String getName();

    /** define which cluster to create this coordinator, auto define when the value is AUTO */
    String cluster();

    boolean asyncSupport();

    boolean remoteSupport();

    /** the url for remote executor to call, it will be auto replaced if blank when it is in-need */
    String url();

    String dataDispatchResultHandler();

    /** the default variables for the process */
    SprayData getDefaultProcessData();

    /** the startNodes for the coordinator */
    List<SprayProcessExecuteStepMeta> listStartNodes();
    List<SprayProcessExecuteStepMeta> listNextNodes(String stepExecutorNameKey);

    List<SprayData> getDefaultDataList();

    String getGeneratorClass();
}
