package top.spray.engine.step.meta;


import org.apache.commons.lang3.StringUtils;
import top.spray.core.engine.exception.SprayMetaError;
import top.spray.core.engine.exception.SprayNotSupportError;
import top.spray.core.engine.execute.SprayExecutorType;
import top.spray.core.engine.execute.SprayStepActiveType;
import top.spray.core.engine.meta.SprayBaseMeta;
import top.spray.core.engine.props.SprayData;
import top.spray.engine.coordinate.coordinator.SprayProcessCoordinator;
import top.spray.engine.coordinate.meta.SprayProcessCoordinatorMeta;
import top.spray.engine.factory.SprayExecutorFactory;
import top.spray.engine.step.condition.SprayStepExecuteConditionFilter;
import top.spray.engine.step.handler.filter.SprayStepExecuteConditionHelper;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 节点引擎
 */
public class SprayProcessStepMeta implements SprayBaseMeta<SprayProcessStepMeta> {
    /* executor meta container */
    private final SprayData metaContainer;

    /* base meta */
    private String id;
    private String name;
    private String executorNameKey;
    private SprayExecutorType executorType;
    private List<SprayProcessStepMeta> nextNodes;
    private String executorGeneratorClass;
    private String executorClass;
    private String jarFiles;

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

    public SprayProcessStepMeta(SprayData metaContainer) {
        this.metaContainer = metaContainer.unmodifiable();
        try {
            init();
        } catch (Exception e) {
            throw new SprayMetaError(this, "failed to init the step meta " + this.getName(), e);
        }
    }

    private void init() throws ClassNotFoundException {
        this.id = metaContainer.getNoneNull("stepId", String.class);
        this.name = metaContainer.getNoneNull("stepName", String.class);
        this.executorType = SprayExecutorType.valueOf(metaContainer.getOrElse("executorType", "COMPUTE"));
        this.nextNodes = metaContainer.getList("nextNodes", SprayProcessStepMeta.class);
        this.executorGeneratorClass = metaContainer.getOrElse("executorGeneratorClass", "");
        this.executorClass = metaContainer.getNoneNull("executorClass", String.class);
        this.jarFiles = metaContainer.getString("jarFiles");
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
        this.executeConditionFilters = SprayStepExecuteConditionHelper.createFilters(this.metaContainer);
    }


    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public String getName() {
        return this.name;
    }

    public String getExecutorNameKey(SprayProcessCoordinatorMeta coordinatorMeta) {
        return executorNameKey == null ?
                (executorNameKey = SprayExecutorFactory.getExecutorNameKey(coordinatorMeta, this))
                : executorNameKey;
    }

    public SprayExecutorType getExecutorType() {
        return executorType;
    }

    @Override
    public String transactionId() {
        throw new SprayNotSupportError(this,
                new IllegalAccessError("couldn't get the transactionId from step meta"));
    }

    public List<SprayProcessStepMeta> nextNodes() {
        return this.nextNodes;
    }

    public Collection<SprayStepExecuteConditionFilter> getExecuteConditionFilter() {
        return executeConditionFilters;
    }

    /**
     * the generator class must implement the interface SprayExecutorGenerator
     */
    public String executorGeneratorClass() {
        return this.executorGeneratorClass;
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
}
