package top.spray.engine.process.processor.execute.step.meta;

import top.spray.common.data.SprayData;
import top.spray.engine.process.infrastructure.meta.SprayMetaError;
import top.spray.engine.process.infrastructure.meta.SprayBaseMeta;
import top.spray.engine.process.processor.dispatch.filters.SprayStepExecuteConditionFilter;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Step Meta
 */
public interface SprayProcessExecuteStepMeta extends SprayBaseMeta, Cloneable {

    @Override
    String getId();

    @Override
    String getName();

    SprayExecutorType getExecutorType();

    List<? extends SprayProcessExecuteStepMeta> nextNodes();

    Collection<SprayStepExecuteConditionFilter> getExecuteConditionFilter();

    /**
     * the generator class must implement the interface SprayExecutorGenerator
     */
    String executorFactoryClass();
    String executorClass();

    /**
     * the jars for running
     */
    String jarFiles();

    /**
     * 1    - run current node <br>
     * 0    - skip current node <br>
     * -1   - skip all from current node
     */
    SprayStepActiveType stepActiveType();

    boolean transactional();

    boolean rollbackIfError();

    boolean ignoreError();

    boolean isAsync();

    /** enable remoting execute */
    boolean isRemoting();

    /**
     * jarFiles for remote executor
     */
    String remotingJarFiles();

    int coreThreadCount();

    int maxThreadCount();

    int queueCapacity();

    int threadAliveTime();

    TimeUnit threadAliveTimeUnit();

    int maxConcurrentReceiving();

    String getBlackEvents();

    String getWhiteEvents();

    String blockHandlerClass();

    /**
     * @return
     *  0 -> no <br>
     *  1 -> easy <br>
     *  2 -> deep
     */
    int varCopyMode();

    <T> T get(String key, Class<T> tClass);

    String getString(String key);

    String getString(String key, String defVal);

    Integer getInteger(String key, Integer defVal);

    Long getLong(String key, Long defVal);

    Boolean getBoolean(String key, Boolean bool);

    SprayData getMetaContainer();

    String toJson();

}
