package kws.panier.front.spi.defaults;

import com.google.common.base.Optional;
import kws.panier.front.api.CacheInfos;
import kws.panier.front.api.Context;
import kws.panier.front.api.ResponsePayload;
import kws.panier.front.spi.CacheInfosResolver;


public class DefaultCacheInfosResolver implements CacheInfosResolver {

    @Override
    public Optional<CacheInfos> cacheInfos(ResponsePayload payload,
                                           Context context) {
        return Optional.absent();
    }

}
