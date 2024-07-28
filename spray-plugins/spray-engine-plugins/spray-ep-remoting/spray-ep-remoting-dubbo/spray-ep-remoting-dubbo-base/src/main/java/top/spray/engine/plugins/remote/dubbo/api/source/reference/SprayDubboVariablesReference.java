package top.spray.engine.plugins.remote.dubbo.api.source.reference;

import top.spray.core.engine.props.SprayData;

public interface SprayDubboVariablesReference {
    SprayData baseInfo(String transactionId, String identityDataKey);
    boolean banKey(String transactionId, String identityDataKey, String key);

    boolean freeKey(String transactionId, String identityDataKey, String key);

    String getJsonString(String transactionId, String identityDataKey, String key);

    String suGetJsonString(String transactionId, String identityDataKey, String key);

    Object get(String transactionId, String identityDataKey, String key) ;
    Object suGet(String transactionId, String identityDataKey, String key) ;
    <T> T get(String transactionId, String identityDataKey, String key, String tClassName) throws Exception;
    <T> T suGet(String transactionId, String identityDataKey, String key, String tClassName) throws Exception;
    <T> Object getOrElse(String transactionId, String identityDataKey, String key, T def) ;
    <T> Object suGetOrElse(String transactionId, String identityDataKey, String key, T def) ;

    <T> T computeIfAbsent(String transactionId, String identityDataKey, String key, T value, boolean ignoreBanned, boolean setBannedIfAbsent) ;

    boolean set(String transactionId, String identityDataKey, String key, Object value, boolean setBanned) ;

    Object remove(String transactionId, String identityDataKey, String key, boolean setBanned) ;

    SprayData copyInto(String transactionId, String identityDataKey, SprayData data);


}

