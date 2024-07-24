package top.spray.engine.remote.dubbo.api;

public interface SprayDubboVariablesReference {

    boolean needWait(String variablesIdentityDataKey, String fromExecutorNameKey, String data, boolean still);

    void execute(String variablesIdentityDataKey, String fromExecutorNameKey, String data, boolean still);

}

