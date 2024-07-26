package top.spray.engine.plugins.remote.dubbo.api.target.reference;

public interface SprayDubboVariablesReference {

    boolean needWait(String variablesIdentityDataKey, String fromExecutorNameKey, String data, boolean still);

    void execute(String variablesIdentityDataKey, String fromExecutorNameKey, String data, boolean still);

}

