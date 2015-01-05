package kws.panier.front.spi;

import com.google.common.base.Optional;
import kws.panier.front.api.CacheInfos;
import kws.panier.front.api.Context;
import kws.panier.front.api.ResponsePayload;


public interface CacheInfosResolver {

    public Optional<CacheInfos> cacheInfos(ResponsePayload payload,
                                           Context context);

}
