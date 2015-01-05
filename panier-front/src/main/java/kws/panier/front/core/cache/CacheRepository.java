package kws.panier.front.core.cache;

import kws.panier.front.api.*;


public interface CacheRepository {

    CacheResult find(CacheId cacheId, CacheReadPolicy policy) throws
            IORuntimeException;

    void insert(CacheInfos cacheInfos, ResponsePayload payload) throws
            IORuntimeException;

}
