package kws.panier.front.core;

import kws.panier.front.core.cache.CacheRepository;
import kws.panier.front.core.cache.InMemoryCacheRepository;
import kws.panier.front.core.immediate.ImmediateRequestService;
import kws.panier.front.core.immediate.InMemoryRequestService;
import kws.panier.front.core.remote.RemoteAmqpService;
import kws.panier.front.core.remote.RemoteListener;
import kws.panier.front.core.remote.RemoteService;
import kws.panier.front.spi.CacheInfosResolver;
import kws.panier.front.spi.PayloadConverter;
import kws.panier.front.spi.ResponseCallback;
import kws.panier.front.spi.defaults.DefaultCacheInfosResolver;
import kws.panier.front.spi.defaults.DefaultPayloadConverter;
import kws.panier.front.spi.defaults.DefaultResponseCallback;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;


public class EngineBuilder {

    private CacheRepository cacheRepository;
    private ImmediateRequestService requestService;
    private RemoteService remoteService;
    private RemoteListener remoteListener;

    private PayloadConverter payloadConverter;
    private CacheInfosResolver cacheInfosResolver;
    private List<ResponseCallback> responseCallbacks;

    public EngineBuilder() {

        payloadConverter = new DefaultPayloadConverter();
        cacheInfosResolver = new DefaultCacheInfosResolver();
        responseCallbacks = new CopyOnWriteArrayList<>();
        responseCallbacks.add(new DefaultResponseCallback());
    }

    public Engine build() {

        Services services = new Services(payloadConverter, cacheInfosResolver, responseCallbacks);

        cacheRepository = new InMemoryCacheRepository();
        requestService = new InMemoryRequestService();
        //TODO READ CONF OR SETTERS
        remoteService = new RemoteAmqpService(null, "EXCHANGE", "ROUTING", payloadConverter);

        return new Engine(cacheRepository, requestService, remoteService, remoteListener, services);
    }

    public void setPayloadConverter(PayloadConverter payloadConverter) {
        this.payloadConverter = payloadConverter;
    }

    public void setCacheInfosResolver(CacheInfosResolver cacheInfosResolver) {
        this.cacheInfosResolver = cacheInfosResolver;
    }

    public void setResponseCallbacks(List<ResponseCallback> responseCallbacks) {
        this.responseCallbacks = responseCallbacks;
    }

}
