package top.spray.engine.plugins.remote.dubbo.provider.target;

import top.spray.engine.plugins.remote.dubbo.api.target.reference.SprayDubboExecutorReference;


public class SprayDubboExecutorReferenceImpl implements SprayDubboExecutorReference {
    private SprayDubboProcessCoordinator dubboCoordinator;
    @Override
    public boolean needWait(String myNameKey, String variablesIdentityDataKey, String fromExecutorNameKey, byte[] data, boolean still) {
        dubboCoordinator
        return false;
    }

    @Override
    public void execute(String myNameKey, String variablesIdentityDataKey, String fromExecutorNameKey, byte[] data, boolean still) {

    }

}
