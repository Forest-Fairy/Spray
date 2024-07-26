package top.spray.engine.plugins.remote.dubbo.api.source;

public interface SprayDubboExecutorReference {

    boolean needWait(String myNameKey, String variablesIdentityDataKey, String fromExecutorNameKey, String data, boolean still);

    void execute(String myNameKey, String variablesIdentityDataKey, String fromExecutorNameKey, String data, boolean still);


}

