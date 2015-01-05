package kws.panier.front.core;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import kws.panier.front.spi.CacheInfosResolver;
import kws.panier.front.spi.PayloadConverter;
import kws.panier.front.spi.ResponseCallback;

import java.util.List;


public class Services {

    private final PayloadConverter payloadConverter;
    private final CacheInfosResolver cacheInfosResolver;
    private final ImmutableList<ResponseCallback> responseCallbacks;

    public Services(
            PayloadConverter payloadConverter,
            CacheInfosResolver cacheInfosResolver,
            List<ResponseCallback> responseCallbacks) {

        Preconditions.checkNotNull(payloadConverter, "Invalid payload encoder");
        this.payloadConverter = payloadConverter;

        Preconditions.checkNotNull(cacheInfosResolver, "Invalid cache infos resolver");
        this.cacheInfosResolver = cacheInfosResolver;

        Preconditions.checkNotNull(responseCallbacks, "Invalid response callbacks");
        this.responseCallbacks = ImmutableList.copyOf(responseCallbacks);
    }

    public PayloadConverter getPayloadConverter() {
        return payloadConverter;
    }

    public CacheInfosResolver getCacheInfosResolver() {
        return cacheInfosResolver;
    }

    public ImmutableList<ResponseCallback> getResponseCallbacks() {
        return responseCallbacks;
    }

}
