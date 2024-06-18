package top.spray.engine.step.meta;


import com.alibaba.fastjson2.JSONObject;
import top.spray.core.engine.exception.SprayMetaError;
import top.spray.core.engine.exception.SprayNotSupportError;
import top.spray.core.engine.execute.SprayStepActiveType;
import top.spray.core.engine.meta.SprayBaseMeta;
import top.spray.core.engine.props.SprayData;
import top.spray.engine.step.condition.SprayStepExecuteConditionFilter;
import top.spray.engine.step.handler.filter.SprayStepExecuteConditionFilterHandler;

import java.util.Collection;
import java.util.List;

/**
 * 节点引擎
 */
public class SprayProcessStepMeta implements SprayBaseMeta<SprayProcessStepMeta> {
    private SprayData dataInside;
    private String id;
    private String name;
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

    public SprayProcessStepMeta(SprayData dataInside) {
        this.dataInside = dataInside.unmodifiable();
        try {
            init();
        } catch (Exception e) {
            throw new SprayMetaError(this, "failed to init the step meta " + this.getName(), e);
        }
    }

    private void init() throws ClassNotFoundException {
        this.id = dataInside.getString("stepId");
        this.name = dataInside.getString("stepName");
        this.nextNodes = dataInside.getList("nextNodes", SprayProcessStepMeta.class);
        this.executorClass = dataInside.getString("executorClass");
        this.jarFiles = dataInside.getString("jarFiles");
        this.stepActiveType = SprayStepActiveType.valueOf(dataInside.get("activeType", "ACTIVE").toUpperCase());
        this.transactional = dataInside.get("isTransactional", false);
        this.rollbackIfError = this.transactional && dataInside.get("rollbackIfError", false);
        // only effect when rollbackIfError is false
        this.ignoreError = (!this.rollbackIfError) && (dataInside.get("ignoreError", false));
        this.isAsync = dataInside.get("isAsync", false);
        this.maxThreadCount = dataInside.get("maxThreadCount", 1);
        this.varCopy = dataInside.get("varCopy", 0);
        this.executeConditionFilters = SprayStepExecuteConditionFilterHandler.createFilters(this.dataInside);
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
        return dataInside.get(key, tClass);
    }

    public String getString(String key) {
        return dataInside.getString(key);
    }

    public String getString(String key, String defVal) {
        return dataInside.get(key, defVal);
    }

    public Integer getInteger(String key, Integer defVal) {
        return dataInside.get(key, defVal);
    }

    public Long getLong(String key, Long defVal) {
        return dataInside.get(key, defVal);
    }

    public Boolean getBoolean(String key, Boolean bool) {
        return dataInside.get(key, bool);
    }

    public SprayData getDataInside() {
        return dataInside;
    }

}
