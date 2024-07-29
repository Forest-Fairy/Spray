package top.spray.engine.plugins.remote.dubbo.provider;

import cn.hutool.cache.CacheListener;
import cn.hutool.cache.impl.TimedCache;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;
import top.spray.core.engine.props.SprayData;
import top.spray.engine.coordinate.coordinator.SprayProcessCoordinator;
import top.spray.engine.plugins.remote.dubbo.api.source.reference.SprayDubboCoordinatorReference;
import top.spray.engine.plugins.remote.dubbo.api.source.reference.SprayDubboVariablesReference;
import top.spray.engine.plugins.remote.dubbo.api.target.SprayDubboCoordinator;
import top.spray.engine.plugins.remote.dubbo.api.target.reference.SprayDubboExecutorFactoryReference;
import top.spray.engine.plugins.remote.dubbo.factory.SprayDubboAdapterFactory;
import top.spray.engine.plugins.remote.dubbo.factory.SprayDubboCoordinatorFactory;
import top.spray.engine.plugins.remote.dubbo.factory.SprayDubboReferenceFactory;
import top.spray.engine.plugins.remote.dubbo.util.SprayDubboConfigurations;
import top.spray.engine.plugins.remote.dubbo.util.SprayDubboDataUtil;
import top.spray.engine.prop.SprayVariableContainer;
import top.spray.engine.step.executor.SprayProcessStepExecutor;

import java.util.Set;

@DubboService
public class SprayBaseService implements
        SprayDubboExecutorFactoryReference,
        SprayDubboCoordinatorReference,
        SprayDubboVariablesReference {
    private static final int MAX_CACHE_TIME = SprayDubboConfigurations.dubboServiceBeanMaxCacheTime();
    private static final TimedCache<String, SprayDubboCoordinator> Coordinators =
            new TimedCache<>(MAX_CACHE_TIME);
    static {
        Coordinators.setListener(new CacheListener<String, SprayDubboCoordinator>() {
            @Override
            public void onRemove(String transactionId, SprayDubboCoordinator coordinator) {
                // TODO deal with executor to remove
                coordinator.closeInRuntime();
            }
        });
    }

    @Override
    public boolean generateExecutor(int dubboServicePort, String transactionId, String executorNameKey, String coordinatorMeta, String executorMeta) {
        SprayDubboCoordinator coordinator = Coordinators.get(transactionId);
        if (coordinator == null) {
            synchronized (Coordinators) {
                if ((coordinator = Coordinators.get(transactionId)) == null) {
                    coordinator = SprayDubboCoordinatorFactory.createSprayDubboCoordinator(dubboServicePort, transactionId, coordinatorMeta);
                }
            }
        }
        try {
            SprayDubboAdapterFactory.generateExecutor(coordinator, executorNameKey, executorMeta);
            return true;
        } catch (Exception e) {
            // TODO handle with ex
            return false;
        }
    }

    @Override
    public boolean whenCoordinatorShutDown(String transactionId) {
        try {
            if (!Coordinators.containsKey(transactionId)) {
                throw new IllegalArgumentException("no such transaction process");
            }
            Coordinators.remove(transactionId);
            return true;
        } catch (Exception e) {
            // handle with ex
            return false;
        }
    }

    @Override
    public int getCoordinatorStatus(String transactionId) {
        return SprayDubboReferenceFactory.getCoordinator(transactionId).status().getCode();
    }

    @Override
    public int createExecutorCount(String transactionId) {
        return SprayDubboReferenceFactory.getCoordinator(transactionId).createExecutorCount();
    }

    @Override
    public void dispatch(String transactionId, String filteredNodeKeys, String variablesIdentityDataKey, String fromExecutorNameKey, byte[] bytes, boolean still) {
        SprayProcessCoordinator coordinator = SprayDubboReferenceFactory.getCoordinator(transactionId);
        SprayProcessStepExecutor executor = coordinator.getStepExecutor(fromExecutorNameKey);
        Set<String> filteredKeys = StringUtils.isBlank(filteredNodeKeys) ?
                Set.of() : Set.of(filteredNodeKeys.split(","));
        coordinator.dispatch(
                variablesIdentityDataKey,
                (current, data, still1, nextStepMeta) -> !filteredKeys
                        .contains(nextStepMeta.getExecutorNameKey(coordinator.getMeta())),
                executor,
                SprayDubboDataUtil.resolveData(coordinator.getMeta(), executor.getMeta(), bytes),
                still);
    }

    @Override
    public SprayData baseInfo(String transactionId, String identityDataKey) {
        SprayVariableContainer variablesContainer = SprayDubboReferenceFactory.getCoordinator(transactionId).getVariablesContainer(identityDataKey);
        return new SprayData()
                .append("createTime", variablesContainer.createTime())
                .append("creator", variablesContainer.creator());
    }

    @Override
    public boolean banKey(String transactionId, String identityDataKey, String key) {
        SprayDubboReferenceFactory.getCoordinator(transactionId).getVariablesContainer(identityDataKey)
                .banKey(key);
        return true;
    }

    @Override
    public boolean freeKey(String transactionId, String identityDataKey, String key) {
        SprayDubboReferenceFactory.getCoordinator(transactionId).getVariablesContainer(identityDataKey)
                .freeKey(key);
        return true;
    }

    @Override
    public String getJsonString(String transactionId, String identityDataKey, String key) {
        return SprayDubboReferenceFactory.getCoordinator(transactionId).getVariablesContainer(identityDataKey)
                .getJsonString(key);
    }

    @Override
    public String suGetJsonString(String transactionId, String identityDataKey, String key) {
        return SprayDubboReferenceFactory.getCoordinator(transactionId).getVariablesContainer(identityDataKey)
                .suGetJsonString(key);
    }

    @Override
    public Object get(String transactionId, String identityDataKey, String key) {
        return SprayDubboReferenceFactory.getCoordinator(transactionId).getVariablesContainer(identityDataKey)
                .get(key);
    }

    @Override
    public Object suGet(String transactionId, String identityDataKey, String key) {
        return SprayDubboReferenceFactory.getCoordinator(transactionId).getVariablesContainer(identityDataKey)
                .suGet(key);
    }

    @Override
    public Object get(String transactionId, String identityDataKey, String key, String tClassName) throws ClassNotFoundException {
        return SprayDubboReferenceFactory.getCoordinator(transactionId).getVariablesContainer(identityDataKey)
                .get(key, Class.forName(tClassName));
    }

    @Override
    public Object suGet(String transactionId, String identityDataKey, String key, String tClassName) throws ClassNotFoundException {
        return SprayDubboReferenceFactory.getCoordinator(transactionId).getVariablesContainer(identityDataKey)
                .get(key, Class.forName(tClassName));
    }

    @Override
    public <T> Object getOrElse(String transactionId, String identityDataKey, String key, T def) {
        return SprayDubboReferenceFactory.getCoordinator(transactionId).getVariablesContainer(identityDataKey)
                .getOrElse(key, def);
    }

    @Override
    public <T> Object suGetOrElse(String transactionId, String identityDataKey, String key, T def) {
        return SprayDubboReferenceFactory.getCoordinator(transactionId).getVariablesContainer(identityDataKey)
                .suGetOrElse(key, def);
    }

    @Override
    public <T> T computeIfAbsent(String transactionId, String identityDataKey, String key, T value, boolean ignoreBanned, boolean setBannedIfAbsent) {
        return SprayDubboReferenceFactory.getCoordinator(transactionId).getVariablesContainer(identityDataKey)
                .computeIfAbsent(key, value, ignoreBanned, setBannedIfAbsent);
    }

    @Override
    public boolean set(String transactionId, String identityDataKey, String key, Object value, boolean setBanned) {
        SprayDubboReferenceFactory.getCoordinator(transactionId).getVariablesContainer(identityDataKey)
                .set(key, value, setBanned);
        return true;
    }

    @Override
    public Object remove(String transactionId, String identityDataKey, String key, boolean setBanned) {
        return SprayDubboReferenceFactory.getCoordinator(transactionId).getVariablesContainer(identityDataKey)
                .remove(key, setBanned);
    }

    @Override
    public SprayData copyInto(String transactionId, String identityDataKey, SprayData data) {
        return SprayDubboReferenceFactory.getCoordinator(transactionId).getVariablesContainer(identityDataKey)
                .copyInto(data);
    }
}

