package kws.panier.front.core;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.util.concurrent.SettableFuture;
import kws.panier.front.api.*;
import kws.panier.front.core.cache.CacheRepository;
import kws.panier.front.core.cache.CacheResult;
import kws.panier.front.core.immediate.ImmediateRequestListener;
import kws.panier.front.core.immediate.ImmediateRequestService;
import kws.panier.front.core.immediate.RequestId;
import kws.panier.front.core.remote.RemoteListener;
import kws.panier.front.core.remote.RemoteService;
import kws.panier.front.spi.CacheInfosResolver;
import kws.panier.front.spi.ResponseCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


public class Engine implements RemoteListener, ImmediateRequestListener {

    private static final Logger LOG = LoggerFactory.getLogger(Engine.class);

    private final Services userServices;
    private final CacheRepository cacheRepository;
    private final ImmediateRequestService immediateRequestService;
    private final RemoteService remoteService;
    private final ExecutorService executor;

    Engine(
            CacheRepository cacheRepository,
            ImmediateRequestService requestService,
            RemoteService remoteService,
            RemoteListener remoteListener,
            Services userServices) {

        Preconditions.checkNotNull(userServices, "Invalid user services");
        this.userServices = userServices;

        Preconditions.checkNotNull(cacheRepository, "Invalid cache repository");
        this.cacheRepository = cacheRepository;

        Preconditions.checkNotNull(requestService, "Invalid request repository");
        this.immediateRequestService = requestService;

        Preconditions.checkNotNull(remoteService, "Invalid remote service");
        this.remoteService = remoteService;

        executor = Executors.newFixedThreadPool(2);
    }

    public void request(
            RequestPayload payload,
            CacheWriteStrategy cacheWriteStrategy,
            Map<String, String> userProperties) {

        Context context = prepareContext(cacheWriteStrategy, userProperties);
        remoteService.send(payload, context);
    }

    public Future<ResponsePayload> immediateRequest(
            RequestPayload payload,
            CacheReadPolicy cacheReadPolicy,
            CacheWriteStrategy cacheWriteStrategy,
            Map<String, String> userProperties) {

        Context context = prepareContext(cacheWriteStrategy, userProperties);

        // Check read cache policy
        boolean mustRefresh = true;
        Optional<ResponsePayload> payloadOpt = Optional.absent();

        Optional<CacheId> cacheIdOpt = findCacheId(context);
        Optional<CacheResult> cacheResultOpt
                = findCacheResult(cacheIdOpt, cacheRepository, cacheReadPolicy);

        if (cacheResultOpt.isPresent()) {
            CacheResult cacheResult = cacheResultOpt.get();
            mustRefresh = !cacheResult.isFresh();
            payloadOpt = cacheResult.getPayload();
        }

        // prepare result
        SettableFuture<ResponsePayload> cachedResponse = null;
        SettableFuture<ResponsePayload> futureResponse = null;

        if (payloadOpt.isPresent()) {
            cachedResponse = SettableFuture.<ResponsePayload>create();
            cachedResponse.set(payloadOpt.get());

        } else {

            if (mustRefresh) {
                // register immediate request future
                futureResponse = SettableFuture.<ResponsePayload>create();
                RequestId requestId = immediateRequestService.register(futureResponse);

                // save request id and send request in background thread
                context.setRequestId(requestId);
                executor.submit(new Requester(remoteService, payload, context));
            }

        }

        // Prepare result
        if (null != cachedResponse) {
            return cachedResponse;

        } else if (null != futureResponse) {
            return futureResponse;

        } else {
            // BAD STATE
            LOG.error("**** INVALID STATE ****\n"
                    + " cachePolicy:{}, cacheId:{}, cacheResult:{}",
                      cacheReadPolicy, cacheIdOpt, cacheResultOpt);
            throw new IllegalStateException("No future response created");
        }
    }

    @Override
    public void onResponse(ResponsePayload payload, Context context) {
        // deal with cache value
        if (context.isCachable()) {
            Optional<CacheInfos> cacheInfosOpt = findCacheInfos(payload, context);

            if (cacheInfosOpt.isPresent()) {
                cacheRepository.insert(cacheInfosOpt.get(), payload);
            }
        }

        // deal with immediate request
        Optional<RequestId> requestId = context.getRequestId();
        if (requestId.isPresent()) {
            this.onImmediateResponse(requestId.get(), payload);
        }

        // deal with user callbacks
        try {
            for (ResponseCallback callback: userServices.getResponseCallbacks()) {
                ResponseCallback.Result res = callback.handle(payload, context);
                if (res == ResponseCallback.Result.STOP) {
                    break;
                }
            }
        } catch (Exception e) {
            LOG.error("Unexpected error in callback execution", e);
        }
    }

    @Override
    public void onImmediateResponse(RequestId requestId, ResponsePayload payload) {
        immediateRequestService.notify(requestId, payload);
    }

    private Context prepareContext(CacheWriteStrategy cacheWriteStrategy,
                                   Map<String, String> userProperties) {
        // prepare request context
        Context context = new Context();
        context.putProperties(userProperties);

        //   set context cache infos
        if (cacheWriteStrategy.isCachable()) {
            context.setCachable(true);

            Optional<CacheInfos> cacheInfosOpt = cacheWriteStrategy.getCacheInfos();
            if (cacheInfosOpt.isPresent()) {
                context.setCacheInfos(cacheInfosOpt.get());
            }
        }
        return context;
    }

    private Optional<CacheId> findCacheId(
            Context context) {
        Optional<CacheInfos> cacheInfos = context.getCacheInfos();
        if (cacheInfos.isPresent()) {
            return Optional.of(cacheInfos.get().getCacheId());
        }
        return Optional.absent();
    }

    private Optional<CacheResult> findCacheResult(Optional<CacheId> cacheId,
                                                  final CacheRepository cacheRepository,
                                                  final CacheReadPolicy cacheReadPolicy) {

        return cacheId.transform(new Function<CacheId, CacheResult>() {
            @Override
            public CacheResult apply(CacheId cacheId) {
                return cacheRepository.find(cacheId, cacheReadPolicy);
            }
        });
    }

    private Optional<CacheInfos> findCacheInfos(ResponsePayload payload,
                                                Context context) {
        Optional<CacheInfos> cacheInfosOpt = context.getCacheInfos();
        if (cacheInfosOpt.isPresent()) {
            return cacheInfosOpt;
        } else {
            CacheInfosResolver cacheInfosResolver = userServices.getCacheInfosResolver();
            return cacheInfosResolver.cacheInfos(payload, context);
        }
    }


    private static class Requester implements Runnable {

        private final RemoteService remoteService;
        private final RequestPayload payload;
        private final Context context;

        public Requester(RemoteService remoteService, RequestPayload payload,
                         Context context) {
            this.remoteService = remoteService;
            this.payload = payload;
            this.context = context;
        }

        @Override
        public void run() {
            remoteService.send(payload, context);
        }

    }

}
