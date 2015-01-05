package kws.panier.front.api;

import com.google.common.base.Optional;


public interface CacheWriteStrategy {

    boolean isCachable();

    Optional<CacheInfos> getCacheInfos();

}
