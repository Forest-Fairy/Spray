package top.spray.engine.remote.dubbo.provider;

import cn.hutool.cache.CacheListener;
import cn.hutool.cache.impl.TimedCache;
import org.apache.dubbo.config.annotation.DubboService;
import top.spray.engine.remote.dubbo.api.SprayDubboCoordinator;
import top.spray.engine.remote.dubbo.api.SprayDubboExecutor;
import top.spray.engine.remote.dubbo.api.SprayDubboInitReference;
import top.spray.engine.remote.dubbo.util.SprayDubboConfigurations;

@DubboService
public class SprayDubboInitReferenceImpl implements SprayDubboInitReference {
    private static final int MAX_CACHE_TIME = SprayDubboConfigurations.dubboServiceBeanMaxCacheTime();
    private static final TimedCache<String, SprayDubboCoordinator> COORDINATORS = new TimedCache<>(MAX_CACHE_TIME);
    private static final TimedCache<String, SprayDubboExecutor> EXECUTORS = new TimedCache<>(MAX_CACHE_TIME);

    static {
        COORDINATORS.setListener(new CacheListener<String, SprayDubboCoordinator>() {
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
        EXECUTORS.setListener(new CacheListener<String, SprayDubboExecutor>() {

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
        SprayDubboExecutor executor = EXECUTORS.get(executorNameKey);
        if (executor != null) {
            return true;
        }
        SprayDubboCoordinator coordinator = COORDINATORS.get(transactionId);
        if (coordinator == null) {
            synchronized (COORDINATORS) {
                if ((coordinator = COORDINATORS.get(transactionId)) == null) {
                    coordinator = createCoordinator(transactionId, coordinatorMeta);
                }
            }
        }
        synchronized (EXECUTORS) {
            if ((executor = EXECUTORS.get(executorNameKey)) == null) {
                executor = createExecutor(coordinator, executorNameKey, executorMeta);
            }
        }
        return executor != null;
    }

    @Override
    public boolean whenCoordinatorShutDown(String transactionId) {
        SprayDubboCoordinator coordinator = COORDINATORS.get(transactionId);
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

        return null;
    }
}
