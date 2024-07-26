package top.spray.engine.prop;

import top.spray.core.engine.props.SprayData;
import top.spray.engine.step.executor.SprayProcessStepExecutor;

public interface SprayVariableContainer {
    String creator() ;

    long createTime() ;

    String identityDataKey() ;

    String nextKey(SprayProcessStepExecutor lastExecutor, SprayProcessStepExecutor curExecutor);

    SprayVariableContainer banKey(String key) ;

    SprayVariableContainer freeKey(String key) ;

    String getJsonString(String key) ;
    String suGetJsonString(String key) ;

    Object get(String key) ;
    Object suGet(String key) ;
    <T> T get(String key, Class<T> tClass) ;
    <T> T suGet(String key, Class<T> tClass) ;
    <T> Object getOrElse(String key, T def) ;
    <T> Object suGetOrElse(String key, T def) ;

    <T> T computeIfAbsent(String key, T value, boolean ignoreBanned, boolean setBannedIfAbsent) ;

    /**
     * set
     * @param setBanned set key banned when get from ancestor
     * @return this
     */
    SprayVariableContainer set(String key, Object value, boolean setBanned) ;

    /**
     * remove
     * @param setBanned set key banned when get from ancestor
     * @return existed value
     */
    Object remove(String key, boolean setBanned) ;

    SprayData copyInto(SprayData data);


    String SEPARATOR = "_";
    static String generateKey(SprayProcessStepExecutor fromExecutor, SprayVariableContainer last, SprayProcessStepExecutor executor) {
        if (fromExecutor == null) {
            // the first execute of the process, last is the process default variables
            return String.format("%s"+SEPARATOR+"%s(%s)", last.identityDataKey(),
                    executor.getMeta().getName(), executor.getExecutorNameKey());
        }
        return String.format("%s"+SEPARATOR+"%s(%s->%s)", last.identityDataKey(),
                executor.getMeta().getName(),
                fromExecutor.getExecutorNameKey(), executor.getExecutorNameKey());
    }
}
