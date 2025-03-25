package top.spray.engine.process.processor.dispatch.coordinate.meta;

import top.spray.common.data.SprayData;
import top.spray.engine.process.processor.dispatch.filters.SprayStepExecuteConditionFilter;
import top.spray.engine.process.processor.execute.step.meta.SprayExecutorType;
import top.spray.engine.process.processor.execute.step.meta.SprayProcessExecuteStepMeta;
import top.spray.engine.process.processor.execute.step.meta.SprayStepActiveType;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class SprayDefaultProcessCoordinatorMeta implements SprayProcessCoordinatorMeta {
    private final SprayData metaContainer;
    private String id;
    private String name;
    private String cluster;
    private boolean asyncSupport;
    private boolean remoteSupport;
    private String url;
    private String dataDispatchResultHandler;
    private SprayData defaultProcessData;
    private List<SprayProcessExecuteStepMeta> startNodes;
    private List<SprayData> defaultDataList;
    private String generatorClass;

    public SprayDefaultProcessCoordinatorMeta(SprayData metaContainer) {
        this.metaContainer = metaContainer.unmodifiable();
        init();
    }

    private void init() {
        this.id = metaContainer.getNoneNull("stepId", String.class);
        this.name = metaContainer.getNoneNull("stepName", String.class);
        this.executorType = SprayExecutorType.valueOf(metaContainer.getOrElse("executorType", "COMPUTE"));
        this.nextNodes = metaContainer.getList("nextNodes", SprayProcessExecuteStepMeta.class);
        this.executorFactoryClass = metaContainer.getOrElse("executorFactoryClass", "");
        this.executorClass = metaContainer.getNoneNull("executorClass", String.class);
        this.jarFiles = metaContainer.getString("jarFiles");
        this.blockHandlerClass = metaContainer.getString("blockHandlerClass");
        this.stepActiveType = SprayStepActiveType.valueOf(metaContainer.getOrElse("activeType", "ACTIVE").toUpperCase());
        this.transactional = metaContainer.getOrElse("isTransactional", false);
        this.rollbackIfError = this.transactional && metaContainer.getOrElse("rollbackIfError", false);
        // only effect when rollbackIfError is false
        this.ignoreError = (!this.rollbackIfError) && (metaContainer.getOrElse("ignoreError", false));
        this.isAsync = metaContainer.getOrElse("isAsync", false);
        this.isRemoting = metaContainer.getOrElse("isRemoting", false);
        this.remotingJarFiles = metaContainer.getString("remotingJarFiles");
        this.coreThreadCount = metaContainer.getOrElse("coreThreadCount", 5);
        this.maxThreadCount = Math.max(this.coreThreadCount, metaContainer.getOrElse("maxThreadCount", 10));
        this.queueCapacity = metaContainer.getOrElse("queueCapacity", 20);
        this.threadAliveTime = metaContainer.getOrElse("threadAliveTime", 30);
        this.threadAliveTimeUnit = TimeUnit.valueOf(metaContainer.getOrElse("threadAliveTimeUnit", "SECONDS"));
        this.maxConcurrentReceiving = metaContainer.getOrElse("maxConcurrentReceiving", 5);
        this.blackEvents = metaContainer.getString("blackEventPassBy");
        this.whiteEvents = metaContainer.getString("whiteEventPassBy");
        this.varCopy = metaContainer.getOrElse("varCopy", 0);
        this.executeConditionFilters = SprayStepExecuteConditionFilter.Factory.createFilters(this);
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String cluster() {
        return this.cluster;
    }

    @Override
    public boolean asyncSupport() {
        return this.asyncSupport;
    }

    @Override
    public boolean remoteSupport() {
        return this.remoteSupport;
    }

    @Override
    public String url() {
        return this.url;
    }

    @Override
    public String dataDispatchResultHandler() {
        return this.dataDispatchResultHandler;
    }

    @Override
    public SprayData getDefaultProcessData() {
        return this.defaultProcessData;
    }

    @Override
    public List<SprayProcessExecuteStepMeta> listStartNodes() {
        return this.startNodes;
    }

    @Override
    public List<SprayProcessExecuteStepMeta> listNextNodes(String stepExecutorNameKey) {
        // Implement logic to return next nodes based on stepExecutorNameKey
        return new ArrayList<>();
    }

    @Override
    public List<SprayData> getDefaultDataList() {
        return this.defaultDataList;
    }

    @Override
    public String getGeneratorClass() {
        return this.generatorClass;
    }
}