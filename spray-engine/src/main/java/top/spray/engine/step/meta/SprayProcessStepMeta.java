package top.spray.engine.step.meta;


import top.spray.core.engine.exception.SprayMetaError;
import top.spray.core.engine.exception.SprayNotSupportError;
import top.spray.core.engine.execute.SprayExecutorType;
import top.spray.core.engine.execute.SprayStepActiveType;
import top.spray.core.engine.meta.SprayBaseMeta;
import top.spray.core.engine.props.SprayData;
import top.spray.engine.step.condition.SprayStepExecuteConditionFilter;
import top.spray.engine.step.handler.filter.SprayStepExecuteConditionHelper;

import java.util.Collection;
import java.util.List;

/**
 * 节点引擎
 */
public class SprayProcessStepMeta implements SprayBaseMeta<SprayProcessStepMeta> {
    private final SprayData metaContainer;
    private String id;
    private String name;
    private SprayExecutorType executorType;
    private List<SprayProcessStepMeta> nextNodes;
    private String executorClass;
    private String jarFiles;
    private SprayStepActiveType stepActiveType;
    private boolean transactional;
    private boolean rollbackIfError;
    private boolean ignoreError;
    private boolean isAsync;
    private int maxThreadCount;
    private int varCopy;
    private Collection<SprayStepExecuteConditionFilter> executeConditionFilters;

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
        this.executorType = SprayExecutorType.valueOf(metaContainer.getIfAbsent("executorType", "COMPUTE"));
        this.nextNodes = metaContainer.getList("nextNodes", SprayProcessStepMeta.class);
        this.executorClass = metaContainer.getNoneNull("executorClass", String.class);
        this.jarFiles = metaContainer.getString("jarFiles");
        this.stepActiveType = SprayStepActiveType.valueOf(metaContainer.getIfAbsent("activeType", "ACTIVE").toUpperCase());
        this.transactional = metaContainer.getIfAbsent("isTransactional", false);
        this.rollbackIfError = this.transactional && metaContainer.getIfAbsent("rollbackIfError", false);
        // only effect when rollbackIfError is false
        this.ignoreError = (!this.rollbackIfError) && (metaContainer.getIfAbsent("ignoreError", false));
        this.isAsync = metaContainer.getIfAbsent("isAsync", false);
        this.maxThreadCount = metaContainer.getIfAbsent("maxThreadCount", 1);
        this.varCopy = metaContainer.getIfAbsent("varCopy", 0);
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
     * 1    - run current node
     * 0    - skip current node
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

    @Override
    public boolean isAsync() {
        return this.isAsync;
    }

    public int maxThreadCount() {
        return this.maxThreadCount;
    }

    /**
     * @return 0 -> no
     * 1 -> simple
     * 2 -> deep
     */
    public int varCopy() {
        return this.varCopy;
    }

    public <T> T get(String key, Class<T> tClass) {
        return metaContainer.get(key, tClass);
    }

    public String getString(String key) {
        return metaContainer.getString(key);
    }

    public String getString(String key, String defVal) {
        return metaContainer.getIfAbsent(key, defVal);
    }

    public Integer getInteger(String key, Integer defVal) {
        return metaContainer.getIfAbsent(key, defVal);
    }

    public Long getLong(String key, Long defVal) {
        return metaContainer.getIfAbsent(key, defVal);
    }

    public Boolean getBoolean(String key, Boolean bool) {
        return metaContainer.getIfAbsent(key, bool);
    }

    public SprayData getMetaContainer() {
        return metaContainer;
    }

}
