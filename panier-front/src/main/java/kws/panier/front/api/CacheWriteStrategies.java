package kws.panier.front.api;

import com.google.common.base.Optional;


public class CacheWriteStrategies {

    private static final NotCachedStrategy NOT_CACHED
            = NotCachedStrategy.instance();
    private static final GuessCacheInfosStrategy GUESS_CACHE_ID
            = GuessCacheInfosStrategy.instance();

    public static CacheWriteStrategy notCached() {
        return NOT_CACHED;
    }

    public static CacheWriteStrategy guessCacheInfos() {
        return GUESS_CACHE_ID;
    }

    public static CacheWriteStrategy useCacheInfos(CacheInfos cacheInfos) {
        return ExplicitCacheInfosStrategy.instance(cacheInfos);
    }


    public static class ExplicitCacheInfosStrategy implements CacheWriteStrategy {

        private final CacheInfos cacheInfos;

        public static ExplicitCacheInfosStrategy instance(CacheInfos cacheInfos) {
            return new ExplicitCacheInfosStrategy(cacheInfos);
        }

        private ExplicitCacheInfosStrategy(CacheInfos cacheInfos) {
            this.cacheInfos = cacheInfos;
        }

        @Override
        public boolean isCachable() {
            return true;
        }

        @Override
        public Optional<CacheInfos> getCacheInfos() {
            return Optional.of(cacheInfos);
        }

    }


    public static class NotCachedStrategy implements CacheWriteStrategy {

        public static NotCachedStrategy instance() {
            return new NotCachedStrategy();
        }

        private NotCachedStrategy() {
        }

        @Override
        public boolean isCachable() {
            return false;
        }

        @Override
        public Optional<CacheInfos> getCacheInfos() {
            return Optional.absent();
        }

    }


    public static class GuessCacheInfosStrategy implements CacheWriteStrategy {

        public static GuessCacheInfosStrategy instance() {
            return new GuessCacheInfosStrategy();
        }

        private GuessCacheInfosStrategy() {
        }

        @Override
        public boolean isCachable() {
            return true;
        }

        @Override
        public Optional<CacheInfos> getCacheInfos() {
            return Optional.absent();
        }

    }

}
