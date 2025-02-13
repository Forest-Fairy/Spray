package top.spray.processor.process.execute.step.meta;

import top.spray.processor.exception.SprayMetaError;
import top.spray.processor.infrustructure.meta.SprayBaseMeta;
import top.spray.processor.process.dispatch.filters.SprayStepExecuteConditionFilter;
import top.spray.core.global.prop.SprayData;


import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 节点引擎
 */
public class SprayProcessExecuteStepMeta implements SprayBaseMeta, Cloneable {

    /* executor meta container */
    private final SprayData metaContainer;

    /* base meta */
    private String id;
    private String name;
    private SprayExecutorType executorType;
    private List<SprayProcessExecuteStepMeta> nextNodes;
    private String executorFactoryClass;
    private String executorClass;
    private String jarFiles;
    private String blockHandlerClass;

    /* dubbo meta */
    private String host;
    private int port;


    /* execution options meta */

    private SprayStepActiveType stepActiveType;
    private boolean transactional;
    private boolean rollbackIfError;
    private boolean ignoreError;
    private boolean isAsync;
    private boolean isRemoting;
    private String remotingJarFiles;
    private int coreThreadCount;
    private int maxThreadCount;
    private int queueCapacity;
    private int threadAliveTime;
    private TimeUnit threadAliveTimeUnit;
    private int maxConcurrentReceiving;
    private int varCopy;
    private Collection<SprayStepExecuteConditionFilter> executeConditionFilters;
    private String blackEvents;
    private String whiteEvents;

    public SprayProcessExecuteStepMeta(SprayData metaContainer) {
        this.metaContainer = metaContainer.unmodifiable();
        try {
            init();
        } catch (Exception e) {
            throw new SprayMetaError(this, "failed to init the step meta " + this.getName(), e);
        }
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

    public SprayExecutorType getExecutorType() {
        return executorType;
    }

    public List<SprayProcessExecuteStepMeta> nextNodes() {
        return this.nextNodes;
    }

    public Collection<SprayStepExecuteConditionFilter> getExecuteConditionFilter() {
        return executeConditionFilters;
    }

    /**
     * the generator class must implement the interface SprayExecutorGenerator
     */
    public String executorFactoryClass() {
        return this.executorFactoryClass;
    }
    public String executorClass() {
        return this.executorClass;
    }

    /**
     * the jars for running
     */
    public String jarFiles() {
        return this.jarFiles;
    }

    /**
     * 1    - run current node <br>
     * 0    - skip current node <br>
     * -1   - skip all from current node
     */
    public SprayStepActiveType stepActiveType() {
        return this.stepActiveType;
    }

    public boolean transactional() {
        return this.transactional;
    }

    public boolean rollbackIfError() {
        return rollbackIfError;
    }

    public boolean ignoreError() {
        return this.ignoreError;
    }

    public boolean isAsync() {
        return this.isAsync;
    }

    /** enable remoting execute */
    public boolean isRemoting() {
        return this.isRemoting;
    }

    /**
     * jarFiles for remote executor
     */
    public String remotingJarFiles() {
        return remotingJarFiles;
    }

    public int coreThreadCount() {
        return coreThreadCount;
    }

    public int maxThreadCount() {
        return maxThreadCount;
    }

    public int queueCapacity() {
        return queueCapacity;
    }

    public int threadAliveTime() {
        return threadAliveTime;
    }

    public TimeUnit threadAliveTimeUnit() {
        return threadAliveTimeUnit;
    }

    public int maxConcurrentReceiving() {
        return maxConcurrentReceiving;
    }

    public String getBlackEvents() {
        return this.blackEvents;
    }

    public String getWhiteEvents() {
        return this.whiteEvents;
    }

    public String blockHandlerClass() {
        return blockHandlerClass;
    }

    /**
     * @return
     *  0 -> no <br>
     *  1 -> easy <br>
     *  2 -> deep
     */
    public int varCopyMode() {
        return this.varCopy;
    }

    public <T> T get(String key, Class<T> tClass) {
        return metaContainer.get(key, tClass);
    }

    public String getString(String key) {
        return metaContainer.getString(key);
    }

    public String getString(String key, String defVal) {
        return metaContainer.getOrElse(key, defVal);
    }

    public Integer getInteger(String key, Integer defVal) {
        return metaContainer.getOrElse(key, defVal);
    }

    public Long getLong(String key, Long defVal) {
        return metaContainer.getOrElse(key, defVal);
    }

    public Boolean getBoolean(String key, Boolean bool) {
        return metaContainer.getOrElse(key, bool);
    }

    public SprayData getMetaContainer() {
        return metaContainer;
    }

    public String toJson() {
        return this.metaContainer.toJson();
    }


    @Override
    public SprayProcessExecuteStepMeta clone() {
        try {
            SprayProcessExecuteStepMeta clone = (SprayProcessExecuteStepMeta) super.clone();
            // TODO: copy mutable state here, so the clone can't change the internals of the original
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
