package kws.panier.front.core.cache;

import kws.panier.front.api.*;
import org.joda.time.DateTime;

import java.util.Map;
import java.util.concurrent.*;


public class InMemoryCacheRepository implements CacheRepository {

    private final ConcurrentHashMap<CacheId, CacheValue> cache;
    private final ScheduledExecutorService executor;

    public InMemoryCacheRepository() {
        cache = new ConcurrentHashMap<>();

        executor = Executors.newSingleThreadScheduledExecutor();
        executor.scheduleWithFixedDelay(new StaleCacheCleaner(), 60, 60, TimeUnit.SECONDS);
    }

    @Override
    public void insert(CacheInfos cacheInfos, ResponsePayload payload) throws
            IORuntimeException {
        cache.put(cacheInfos.getCacheId(),
                  new CacheValue(payload,
                                 cacheInfos.getFreshDuration(),
                                 cacheInfos.getStaleDuration()));
    }

    @Override
    public CacheResult find(CacheId cacheId, CacheReadPolicy policy) throws
            IORuntimeException {
        if (policy == CacheReadPolicy.NO_CACHE) {
            return CacheResult.missing();
        }

        if (cache.containsKey(cacheId)) {

            CacheValue cacheValue = cache.get(cacheId);
            if (cacheValue.freshExpirationDate.isAfterNow()) {
                return CacheResult.fresh(cacheValue.payload);

            } else {
                if (policy == CacheReadPolicy.ANY_CACHE) {
                    return CacheResult.stale(cacheValue.payload);
                }
            }
        }
        return CacheResult.missing();
    }


    private static class CacheValue {

        private final ResponsePayload payload;
        private final DateTime freshExpirationDate;
        private final DateTime staleExpirationDate;

        public CacheValue(ResponsePayload payload, long freshDuration,
                          long staleDuration) {
            this.payload = payload;
            DateTime now = DateTime.now();
            freshExpirationDate = now.plus(freshDuration);
            staleExpirationDate = now.plus(staleDuration);
        }

    }


    private class StaleCacheCleaner implements Runnable {

        @Override
        public void run() {
            for (Map.Entry<CacheId, CacheValue> entry: cache.entrySet()) {
                CacheValue value = entry.getValue();
                if (value.staleExpirationDate.isBeforeNow()) {
                    cache.remove(entry.getKey(), value);
                }
            }
        }

    }

}
