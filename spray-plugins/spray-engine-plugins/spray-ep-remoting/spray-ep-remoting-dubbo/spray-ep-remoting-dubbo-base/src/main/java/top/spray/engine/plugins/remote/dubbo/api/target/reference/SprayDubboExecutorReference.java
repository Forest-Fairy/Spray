package top.spray.engine.plugins.remote.dubbo.api.target.reference;

import top.spray.core.engine.props.SprayData;

public interface SprayDubboExecutorReference {

    boolean needWait(String myNameKey, String variablesIdentityDataKey, String fromExecutorNameKey, byte[] data, boolean still);

    void execute(String myNameKey, String variablesIdentityDataKey, String fromExecutorNameKey, byte[] data, boolean still);

}

