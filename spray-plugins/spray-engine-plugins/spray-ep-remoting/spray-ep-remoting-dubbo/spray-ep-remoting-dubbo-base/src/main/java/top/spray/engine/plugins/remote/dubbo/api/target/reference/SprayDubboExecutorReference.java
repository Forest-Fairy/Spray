package top.spray.engine.plugins.remote.dubbo.api.target.reference;

import org.apache.dubbo.common.URL;
import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ProtocolConfig;
import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.config.ServiceConfig;
import org.apache.dubbo.config.bootstrap.DubboBootstrap;
import top.spray.core.dynamic.loader.SprayClassLoader;
import top.spray.engine.coordinate.coordinator.SprayProcessCoordinator;
import top.spray.engine.coordinate.meta.SprayProcessCoordinatorMeta;
import top.spray.engine.plugins.remote.dubbo.api.source.SprayDubboExecutor;
import top.spray.engine.plugins.remote.dubbo.api.target.SprayDubboCoordinator;
import top.spray.engine.plugins.remote.dubbo.constants.SprayDubboConfigConst;
import top.spray.engine.plugins.remote.dubbo.constants.SprayDubboProtocolConst;
import top.spray.engine.plugins.remote.dubbo.consumer.target.SprayDubboSrcExecutorFacade;
import top.spray.engine.plugins.remote.dubbo.provider.target.SprayDubboExecutorTargetReference;
import top.spray.engine.plugins.remote.dubbo.util.SprayDubboConfigurations;
import top.spray.engine.step.executor.SprayExecutorDefinition;
import top.spray.engine.step.meta.SprayProcessStepMeta;

public interface SprayDubboExecutorReference {

    static SprayDubboExecutorReference createTargetReference(SprayProcessCoordinatorMeta coordinatorMeta,
                                                             SprayProcessStepMeta stepMeta, SprayClassLoader classLoader) {
        // TODO get by config
        ReferenceConfig<SprayDubboExecutorReference> referenceConfig = new ReferenceConfig<>();
        referenceConfig.setId(stepMeta.getExecutorNameKey(coordinatorMeta));
        referenceConfig.setInterface(SprayDubboExecutorReference.class);
        referenceConfig.setUrl(stepMeta.getString(SprayDubboConfigConst.KEY_URL));
        URL url = URL.valueOf(referenceConfig.getUrl());
        referenceConfig.setProtocol(url.getProtocol());
        referenceConfig.setAsync(true);
        referenceConfig.setTimeout(stepMeta.getInteger(SprayDubboConfigConst.KEY_TIMEOUT,
                SprayDubboConfigurations.dubboExecutorDefaultTimeOut()));
        return referenceConfig.get();
    }

    static SprayDubboExecutorReference createSrcReference(SprayProcessCoordinatorMeta coordinatorMeta,
                                                             SprayProcessStepMeta stepMeta, SprayClassLoader classLoader) {
        // TODO get by config
        ReferenceConfig<SprayDubboExecutorReference> referenceConfig = new ReferenceConfig<>();
        referenceConfig.setId(stepMeta.getExecutorNameKey(coordinatorMeta));
        referenceConfig.setInterface(SprayDubboExecutorReference.class);
        referenceConfig.setUrl(stepMeta.getString(SprayDubboConfigConst.KEY_URL));
        URL url = URL.valueOf(referenceConfig.getUrl());
        referenceConfig.setProtocol(url.getProtocol());
        referenceConfig.setAsync(true);
        referenceConfig.setTimeout(stepMeta.getInteger(SprayDubboConfigConst.KEY_TIMEOUT,
                SprayDubboConfigurations.dubboExecutorDefaultTimeOut()));
        return referenceConfig.get();
    }

    static void createTargetProvider(String executorNameKey, SprayDubboCoordinator coordinator, SprayExecutorDefinition realExecutor) {
        SprayDubboExecutorTargetReference referenceImpl = new SprayDubboExecutorTargetReference(coordinator, realExecutor);
        ServiceConfig<SprayDubboExecutorReference> serviceConfig = new ServiceConfig<>();
        serviceConfig.setId(executorNameKey);
        serviceConfig.setInterface(SprayDubboExecutor.class);
        serviceConfig.setAsync(true);
        serviceConfig.setRef(referenceImpl);
        DubboBootstrap bootstrap = DubboBootstrap.getInstance();
        bootstrap.application(new ApplicationConfig(SprayDubboConfigurations.dubboApplicationName()))
                .protocol(new ProtocolConfig(
                        realExecutor.getMeta().getString(SprayDubboConfigConst.KEY_PROTOCOL,
                                SprayDubboProtocolConst.DUBBO.getValue()),
                        realExecutor.getMeta().getInteger(SprayDubboConfigConst.KEY_PORT,
                                SprayDubboConfigurations.dubboServiceProviderPort())
                ))
                .service(serviceConfig)
                .start();
    }

    static void createSrcProvider(String executorNameKey, SprayProcessCoordinator coordinator, SprayExecutorDefinition realExecutor) {
        SprayDubboExecutorTargetReference referenceImpl = new SprayDubboSrcExecutorFacade(coordinator, realExecutor);
        ServiceConfig<SprayDubboExecutorReference> serviceConfig = new ServiceConfig<>();
        serviceConfig.setId(executorNameKey);
        serviceConfig.setInterface(SprayDubboExecutor.class);
        serviceConfig.setAsync(true);
        serviceConfig.setRef(referenceImpl);
        DubboBootstrap bootstrap = DubboBootstrap.getInstance();
        bootstrap.application(new ApplicationConfig(SprayDubboConfigurations.dubboApplicationName()))
                .protocol(new ProtocolConfig(
                        realExecutor.getMeta().getString(SprayDubboConfigConst.KEY_PROTOCOL,
                                SprayDubboProtocolConst.DUBBO.getValue()),
                        realExecutor.getMeta().getInteger(SprayDubboConfigConst.KEY_PORT,
                                SprayDubboConfigurations.dubboServiceProviderPort())
                ))
                .service(serviceConfig)
                .start();
    }

    static boolean hasProvider(String transactionId, String executorNameKey) {
        return DubboBootstrap.getInstance().getCache().get(executorNameKey) != null;
    }


    boolean needWait(String myNameKey, String variablesIdentityDataKey, String fromExecutorNameKey, byte[] data, boolean still);

    void execute(String myNameKey, String variablesIdentityDataKey, String fromExecutorNameKey, byte[] data, boolean still);

}

