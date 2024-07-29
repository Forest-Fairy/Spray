package top.spray.engine.plugins.remote.dubbo.api.target;

import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.config.bootstrap.DubboBootstrap;
import top.spray.engine.plugins.remote.dubbo.api.source.reference.SprayDubboCoordinatorReference;
import top.spray.engine.plugins.remote.dubbo.api.source.reference.SprayDubboVariablesReference;
import top.spray.engine.plugins.remote.dubbo.api.target.reference.SprayDubboExecutorFactoryReference;
import top.spray.engine.plugins.remote.dubbo.api.target.reference.SprayDubboExecutorReference;
import top.spray.engine.plugins.remote.dubbo.constants.SprayDubboConfigConst;
import top.spray.engine.plugins.remote.dubbo.util.SprayDubboConfigurations;
import top.spray.engine.step.meta.SprayProcessStepMeta;

public interface SprayDubboBaseService extends
        SprayDubboExecutorFactoryReference,
        SprayDubboCoordinatorReference,
        SprayDubboVariablesReference {

    static SprayDubboBaseService get(SprayDubboCoordinator dubboCoordinator) {
        ReferenceConfig<SprayDubboBaseService> config = new ReferenceConfig<>();
        config.setInterface(SprayDubboBaseService.class);
        config.setUrl(dubboCoordinator.url());
        config.setProtocol(dubboCoordinator.protocol());
        config.setTimeout(SprayDubboConfigurations.dubboExecutorDefaultTimeOut());
        // this should be async because it will be called by many executors
        config.setAsync(true);
        return config.get();
    }

    static SprayDubboBaseService get(SprayProcessStepMeta stepMeta) {
        ReferenceConfig<SprayDubboBaseService> referenceConfig = new ReferenceConfig<>();
        // TODO build the reference config
        referenceConfig.setInterface(SprayDubboBaseService.class);
        String url = stepMeta.getString(SprayDubboConfigConst.KEY_URL);
        referenceConfig.setUrl(url);
        referenceConfig.setTimeout(stepMeta.getInteger(SprayDubboConfigConst.KEY_TIMEOUT,
                SprayDubboConfigurations.dubboExecutorDefaultTimeOut()));
        referenceConfig.setAsync(true);
        DubboBootstrap.getInstance().reference(referenceConfig).start();
        return referenceConfig.get();
    }
}
