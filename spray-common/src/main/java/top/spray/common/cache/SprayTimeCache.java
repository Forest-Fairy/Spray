package top.spray.common.cache;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.Expiry;
import org.checkerframework.checker.index.qual.NonNegative;
import top.spray.common.tools.SpraySynchronizeUtils;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

public class SprayTimeCache {
    public static final SprayCache INSTANCE = new SprayCache() {
        @Override
        public Object setCache(String namespace, String key, Object value, Object... params) {
            Long msDuration = (Long) params[0];
            ExpireStrategy[] expireStrategies = params.length > 1
                    ? (ExpireStrategy[]) Arrays.copyOfRange(params, 1, params.length)
                    : new ExpireStrategy[0];
            Cache<String, SprayTimeCache> Cache = GetCache(namespace);
            SprayTimeCache old = Cache.getIfPresent(key);
            Cache.put(key, NewCache(msDuration, key, value, expireStrategies));
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
        public <T> T computeIfAbsent(String namespace, String key, Function<String, T> supplier, Object... params) {
            Long msDuration = (Long) params[0];
            ExpireStrategy[] expireStrategies = params.length > 1
                    ? (ExpireStrategy[]) Arrays.copyOfRange(params, 1, params.length)
                    : new ExpireStrategy[0];
            Cache<String, SprayTimeCache> Cache = GetCache(namespace);
            SprayTimeCache present = Cache.getIfPresent(key);
            if (present == null) {
                synchronized (Cache) {
                    if ((present = Cache.getIfPresent(key)) == null) {
                        Cache.put(key, present = NewCache(msDuration, key, supplier.apply(key), expireStrategies));
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
            cache.clear();
            Cache.invalidate(key);
            return value;
        }
    };
        public static Object set(String namespace, String key, Object value, long msDuration, ExpireStrategy... expireStrategies) {
            return INSTANCE.setCache(namespace, key, value, msDuration, expireStrategies);
        }

        public static <T> T computeIfAbsent(String namespace, String key, Function<String, T> func, long msDuration, ExpireStrategy... expireStrategies) {
            return INSTANCE.computeIfAbsent(namespace, key, func, msDuration, expireStrategies);
        }

        public static Object get(String namespace, String key) {
            return INSTANCE.getCache(namespace, key);
        }

        public static Object remove(String namespace, String key) {
            return INSTANCE.removeCache(namespace, key);
        }

        private static SprayTimeCache NewCache(Long expireDuration, String key, Object value, ExpireStrategy... expireStrategies) {
            if (expireDuration == null || expireDuration == 0L) {
                throw new IllegalArgumentException("expireDuration must be greater than 0");
            }
            return new SprayTimeCache(expireDuration, key, value, expireStrategies);
        }

        private long nanoExpireDuration;
        private String key;
        private Object value;
        private Set<ExpireStrategy> expireStrategies;

        private SprayTimeCache(Long expireDuration, String key, Object value, ExpireStrategy... expireStrategies) {
            this.nanoExpireDuration = TimeUnit.MILLISECONDS.toNanos(expireDuration);
            this.key = key;
            this.value = value;
            this.expireStrategies = expireStrategies == null || expireStrategies.length == 0
                    ? Set.of(ExpireStrategy.REFRESH_WHEN_UPDATE)
                    : Set.of(expireStrategies);
        }

        private void clear() {
            this.value = null;
            this.key = null;
            this.nanoExpireDuration = 0L;
            this.expireStrategies = null;
        }

        private static final Map<String, Cache<String, SprayTimeCache>> POOL = new HashMap<>(16);

        private static Cache<String, SprayTimeCache> GetCache(String namespace) {
            return SpraySynchronizeUtils.synchronizeGet(POOL, namespace,
                    () -> Caffeine.newBuilder().expireAfter(TimeCacheExpiry.INSTANCE).build());
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