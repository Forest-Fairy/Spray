package top.spray.engine.plugins.remote.dubbo.provider.target;

import cn.hutool.cache.CacheListener;
import cn.hutool.cache.impl.TimedCache;
import org.apache.dubbo.config.annotation.DubboService;
import top.spray.engine.plugins.remote.dubbo.api.target.reference.SprayDubboExecutorFactoryReference;
import top.spray.engine.plugins.remote.dubbo.util.SprayDubboConfigurations;
import top.spray.engine.plugins.remote.dubbo.api.target.SprayDubboCoordinator;
import top.spray.engine.plugins.remote.dubbo.api.source.SprayDubboExecutor;

@DubboService
public class SprayDubboExecutorFactoryReferenceImpl implements SprayDubboExecutorFactoryReference {
    private static final int MAX_CACHE_TIME = SprayDubboConfigurations.dubboServiceBeanMaxCacheTime();
    private static final TimedCache<String, SprayDubboCoordinator> Coordinators = new TimedCache<>(MAX_CACHE_TIME);
    private static final TimedCache<String, SprayDubboExecutor> Executors = new TimedCache<>(MAX_CACHE_TIME);

    static {
        Coordinators.setListener(new CacheListener<String, SprayDubboCoordinator>() {
            @Override
            public void onRemove(String key, SprayDubboCoordinator coordinator) {
                // TODO deal with coordinator to remove
                String transactionId = key;
                try {
                    coordinator.close();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
        Executors.setListener(new CacheListener<String, SprayDubboExecutor>() {

            @Override
            public void onRemove(String key, SprayDubboExecutor executor) {
                // TODO deal with executor to remove
                String[] infos = key.split(",");
                String transactionId = infos[0];
                String executorNameKey = infos[1];
                try {
                    executor.close();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    @Override
    public boolean generateExecutor(String transactionId, String executorNameKey, String coordinatorMeta, String executorMeta) {
        SprayDubboExecutor executor = Executors.get(executorNameKey);
        if (executor != null) {
            return true;
        }
        SprayDubboCoordinator coordinator = Coordinators.get(transactionId);
        if (coordinator == null) {
            synchronized (Coordinators) {
                if ((coordinator = Coordinators.get(transactionId)) == null) {
                    Coordinators.put(transactionId,
                            coordinator = createCoordinator(transactionId, coordinatorMeta));
                }
            }
        }
        synchronized (Executors) {
            if ((executor = Executors.get(executorNameKey)) == null) {
                Executors.put(executorNameKey,
                        executor = createExecutor(coordinator, executorNameKey, executorMeta));
            }
        }
        return executor != null;
    }

    @Override
    public boolean whenCoordinatorShutDown(String transactionId) {
        SprayDubboCoordinator coordinator = Coordinators.get(transactionId);
        if (coordinator != null) {
            try {
                coordinator.close();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return true;
    }


    private SprayDubboCoordinator createCoordinator(String transactionId, String coordinatorMeta) {

    }
    private SprayDubboExecutor createExecutor(SprayDubboCoordinator coordinator, String executorNameKey, String executorMeta) {

    }
}
