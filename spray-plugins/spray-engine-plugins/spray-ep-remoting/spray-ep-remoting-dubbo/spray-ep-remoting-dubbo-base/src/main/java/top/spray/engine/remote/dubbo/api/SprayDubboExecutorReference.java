package top.spray.engine.remote.dubbo.api;

public interface SprayDubboExecutorReference {

    boolean needWait(String myNameKey, String variablesIdentityDataKey, String fromExecutorNameKey, String data, boolean still);

    void execute(String myNameKey, String variablesIdentityDataKey, String fromExecutorNameKey, String data, boolean still);

}

