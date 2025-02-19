package top.spray.common.cache;

import com.github.benmanes.caffeine.cache.*;
import org.checkerframework.checker.index.qual.NonNegative;
import org.checkerframework.checker.nullness.qual.Nullable;
import top.spray.common.tools.SpraySynchronizeUtils;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.function.Supplier;

public class SprayTimeCache {
    public static final SprayCache INSTANCE = new SprayCache() {
        @Override
        public Object setCache(SprayCacheNotifier notifier, String namespace, String key, Object value, Object... params) {
            Long msDuration = (Long) params[0];
            ExpireStrategy[] expireStrategies = params.length > 1
                    ? (ExpireStrategy[]) Arrays.copyOfRange(params, 1, params.length)
                    : new ExpireStrategy[0];
            Cache<String, SprayTimeCache> Cache = GetCache(namespace);
            SprayTimeCache old = Cache.getIfPresent(key);
            Cache.put(key, NewCache(notifier, msDuration, key, value, expireStrategies));
            if (old == null) {
                return null;
            }
            Object oldVal = old.value;
            old.clear();
            return oldVal;
        }

        @Override
        public Object getCache(String namespace, String key) {
            Cache<String, SprayTimeCache> Cache = GetCache(namespace);
            SprayTimeCache cache = Cache.getIfPresent(key);
            if (cache == null) {
                return null;
            }
            return cache.value;
        }

        @Override
        public <T> T compute(Supplier<SprayCacheNotifier> supplierWhenNewOne, String namespace, String key, Function<Object, T> computeFunc, Object... params) {
            Cache<String, SprayTimeCache> Cache = GetCache(namespace);
            SprayTimeCache present = Cache.getIfPresent(key);
            T t = computeFunc.apply(present == null ? null : present.value);
            Long msDuration = (Long) params[0];
            ExpireStrategy[] expireStrategies = params.length > 1
                    ? (ExpireStrategy[]) Arrays.copyOfRange(params, 1, params.length)
                    : new ExpireStrategy[0];
            Cache.put(key, present = NewCache(supplierWhenNewOne.get(), msDuration, key, t, expireStrategies));
            // noinspection unchecked
            return (T) present.value;
        }

        @Override
        public <T> T computeIfAbsent(Supplier<SprayCacheNotifier> supplierWhenNewOne, String namespace, String key, Supplier<T> supplier, Object... params) {
            Cache<String, SprayTimeCache> Cache = GetCache(namespace);
            SprayTimeCache present = Cache.getIfPresent(key);
            if (present == null) {
                synchronized (Cache) {
                    if ((present = Cache.getIfPresent(key)) == null) {
                        Long msDuration = (Long) params[0];
                        ExpireStrategy[] expireStrategies = params.length > 1
                                ? (ExpireStrategy[]) Arrays.copyOfRange(params, 1, params.length)
                                : new ExpireStrategy[0];
                        Cache.put(key, present = NewCache(supplierWhenNewOne.get(), msDuration, key, supplier.get(), expireStrategies));
                    }
                }
            }
            // noinspection unchecked
            return (T) present.value;
        }

        @Override
        public Object removeCache(String namespace, String key) {
            Cache<String, SprayTimeCache> Cache = GetCache(namespace);
            SprayTimeCache cache = Cache.getIfPresent(key);
            if (cache == null) {
                return null;
            }
            Object value = cache.value;
            Cache.invalidate(key);
            return value;
        }
    };
    public static Object set(SprayCacheNotifier notifier, String namespace, String key, Object value, long msDuration, ExpireStrategy... expireStrategies) {
        return INSTANCE.setCache(notifier, namespace, key, value, msDuration, expireStrategies);
    }

    public static <T> T compute(Supplier<SprayCacheNotifier> supplierWhenNewOne, String namespace, String key, Function<Object, T> computeFunc, long msDuration, ExpireStrategy... expireStrategies) {
        return INSTANCE.compute(supplierWhenNewOne, namespace, key, computeFunc, msDuration, expireStrategies);
    }

    public static <T> T computeIfAbsent(Supplier<SprayCacheNotifier> supplierWhenNewOne, String namespace, String key, Supplier<T> supplier, long msDuration, ExpireStrategy... expireStrategies) {
        return INSTANCE.computeIfAbsent(supplierWhenNewOne, namespace, key, supplier, msDuration, expireStrategies);
    }

    public static Object get(String namespace, String key) {
        return INSTANCE.getCache(namespace, key);
    }

    public static Object remove(String namespace, String key) {
        return INSTANCE.removeCache(namespace, key);
    }

    private static SprayTimeCache NewCache(SprayCacheNotifier notifier, Long expireDuration, String key, Object value, ExpireStrategy... expireStrategies) {
        if (expireDuration == null || expireDuration == 0L) {
            throw new IllegalArgumentException("expireDuration must be greater than 0");
        }
        return new SprayTimeCache(notifier, expireDuration, key, value, expireStrategies);
    }
    public static final class Status {
        private Status() {}
        public static final int CREATED = 1;
        public static final int REPLACED = 1;
        public static final int EXPIRED = 1;
        public static final int REMOVED = 1;
    }
    private SprayCacheNotifier notifier;
    private long nanoExpireDuration;
    private String key;
    private Object value;
    private Set<ExpireStrategy> expireStrategies;

    private SprayTimeCache(SprayCacheNotifier notifier, Long expireDuration, String key, Object value, ExpireStrategy... expireStrategies) {
        this.notifier = notifier;
        this.nanoExpireDuration = TimeUnit.MILLISECONDS.toNanos(expireDuration);
        this.key = key;
        this.value = value;
        this.expireStrategies = expireStrategies == null || expireStrategies.length == 0
                ? Set.of(ExpireStrategy.REFRESH_WHEN_UPDATE)
                : Set.of(expireStrategies);
        this.notifyIfNotifierExist(Status.CREATED);
    }

    private void clear() {
        this.value = null;
        this.key = null;
        this.nanoExpireDuration = 0L;
        this.expireStrategies = null;
    }
    private void notifyIfNotifierExist(int status) {
        if (this.notifier != null) {
            this.notifier.notify(this.key, this.value, status);
        }
    }

    private static final Map<String, Cache<String, SprayTimeCache>> POOL = new HashMap<>(16);

    private static Cache<String, SprayTimeCache> GetCache(String namespace) {
        return SpraySynchronizeUtils.synchronizeGet(POOL, namespace,
                () -> Caffeine.newBuilder()
                        .expireAfter(TimeCacheExpiry.INSTANCE)
                        .evictionListener(TimeCacheRemovalListener.INSTANCE)
                        .removalListener(TimeCacheRemovalListener.INSTANCE)
                        .build());
    }
    private static class TimeCacheRemovalListener implements RemovalListener<String, SprayTimeCache> {
        private static final TimeCacheRemovalListener INSTANCE = new TimeCacheRemovalListener();
        @Override
        public void onRemoval(@Nullable String key, @Nullable SprayTimeCache cache, RemovalCause cause) {
            if (cache != null) {
                if (RemovalCause.REPLACED.equals(cause) || RemovalCause.EXPLICIT.equals(cause)) {
                    cache.notifyIfNotifierExist(Status.REPLACED);
                } else if (RemovalCause.EXPIRED.equals(cause)) {
                    cache.notifyIfNotifierExist(Status.EXPIRED);
                } else {
                    cache.notifyIfNotifierExist(Status.REMOVED);
                }
                cache.clear();
            }
        }
    }

    public enum ExpireStrategy {
        REFRESH_WHEN_UPDATE,
        REFRESH_WHEN_ACCESS,
        ;
    }

    private static class TimeCacheExpiry implements Expiry<String, SprayTimeCache> {
        private static final TimeCacheExpiry INSTANCE = new TimeCacheExpiry();
        @Override
        public long expireAfterCreate(String key, SprayTimeCache value, long currentTime) {
            // expire after value.nanoExpireDuration from currentTime
            return currentTime + value.nanoExpireDuration;
        }

        @Override
        public long expireAfterUpdate(String key, SprayTimeCache value, long currentTime, @NonNegative long currentDuration) {
            if (value.expireStrategies.contains(ExpireStrategy.REFRESH_WHEN_UPDATE)) {
                return currentTime + value.nanoExpireDuration;
            } else {
                return currentTime + value.nanoExpireDuration - currentDuration;
            }
        }

        @Override
        public long expireAfterRead(String key, SprayTimeCache value, long currentTime, @NonNegative long currentDuration) {
            if (value.expireStrategies.contains(ExpireStrategy.REFRESH_WHEN_ACCESS)) {
                return currentTime + value.nanoExpireDuration;
            } else {
                return currentTime + value.nanoExpireDuration - currentDuration;
            }
        }
    }
}