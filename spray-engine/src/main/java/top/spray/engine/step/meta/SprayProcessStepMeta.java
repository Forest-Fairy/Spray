package top.spray.engine.step.meta;

import cn.hutool.poi.excel.cell.setters.RichTextCellSetter;
import top.spray.core.engine.exception.SprayMetaError;
import top.spray.core.engine.meta.SprayBaseMeta;
import top.spray.core.engine.props.SprayData;
import top.spray.core.util.SprayClassLoader;
import top.spray.engine.step.executor.SprayProcessStepExecutor;
import top.spray.engine.step.executor.transaction.SprayTransactionSupportExecutor;

import java.util.Date;
import java.util.List;

/**
 * 节点引擎
 */
public class SprayProcessStepMeta implements SprayBaseMeta<SprayProcessStepMeta> {
    private SprayData dataInside;
    private String id;
    private String name;
    private boolean transactional;
    private boolean ignoreError;
    private List<SprayProcessStepMeta> nextNodes;
    private String executorClass;
    private String jarFiles;

    public SprayProcessStepMeta(SprayData dataInside) {
        this.dataInside = dataInside.unmodifiable();
        try {
            init();
        } catch (Exception e) {
            throw new SprayMetaError(this, "failed to init the step meta " + this.getName(), e);
        }
    }

    /**
     * @return
     *  0 -> no
     *  1 -> simple
     *  2 -> deep
     */
    public int varCopy() {
        return dataInside.getInteger("varCopy", 0);
    }

    private void init() throws ClassNotFoundException {
        this.id = dataInside.getString("id");
        this.name = dataInside.getString("name");
        if (SprayTransactionSupportExecutor.class.isAssignableFrom(this.executorClass())) {
            // 执行器支持事务
            this.transactional = dataInside.get("isTransactional", false);
            this.rollbackIfError = this.transactional && dataInside.get("rollbackIfError", false);
        } else {
            this.transactional = false;
            this.rollbackIfError = false;
        }
        // effect when rollbackIfError is false
        this.ignoreError = (!this.rollbackIfError) &&
                (dataInside.get("ignoreError", false));
        this.nextNodes = dataInside.getList("nextNodes", SprayProcessStepMeta.class);
        this.executorClass = dataInside.getString("executorClass");
        this.jarFiles = dataInside.getString("jarFiles");

    }

    public List<SprayProcessStepMeta> nextNodes() {
        return this.nextNodes;
    }

    Class<? extends SprayProcessStepExecutor> executorClass();

    String get(String key);
    Integer getInteger(String key, Integer defVal);
    Boolean getBoolean(String key, Boolean bool);
    Date getDate(String key, Date defVal);

    /** the jars for running */
    String jarFiles();
}
