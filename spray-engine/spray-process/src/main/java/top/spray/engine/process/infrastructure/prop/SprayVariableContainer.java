package top.spray.engine.process.infrastructure.prop;

import top.spray.common.data.SprayData;
import top.spray.common.tools.tuple.SprayTuples;
import top.spray.engine.process.processor.execute.step.executor.facade.SprayStepFacade;

import java.util.function.Supplier;

public interface SprayVariableContainer {
    String creator() ;

    long createTime() ;

    String identityDataKey() ;

    String nextKey(SprayStepFacade lastExecutor, SprayStepFacade curExecutor);

    /**
     * if the key is banned, the suGet method will be unable to get it
     */
    SprayVariableContainer ban(String key) ;
    SprayVariableContainer free(String key) ;

    String getJsonString(String key) ;
    String suGetJsonString(String key) ;

    Object get(String key) ;
    Object suGet(String key) ;
    <T> T get(String key, Class<T> tClass) ;
    <T> T suGet(String key, Class<T> tClass) ;
    <T> Object getOrElse(String key, T def) ;
    <T> Object suGetOrElse(String key, T def) ;

    <T> T computeIfAbsent(String key, boolean ignoreBanned, Supplier<SprayTuples._2<T, Boolean>> valueAndSetBanned) ;

    /**
     * set
     * @param setBanned set key banned when get from ancestor, equal to {@link #ban(String)}
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

    static String generateKey(SprayStepFacade fromExecutor, SprayVariableContainer last, SprayStepFacade executor) {
        if (fromExecutor == null) {
            // the first execute of the process, last is the process default variables
            return String.format("%s"+SEPARATOR+"%s(%s)", last.identityDataKey(),
                    executor.getMeta().getName(), executor.executorNameKey());
        }
        return String.format("%s"+SEPARATOR+"%s(%s->%s)", last.identityDataKey(),
                executor.getMeta().getName(),
                fromExecutor.executorNameKey(), executor.executorNameKey());
    }
}
