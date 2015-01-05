package kws.panier.front.core.immediate;

import com.google.common.util.concurrent.SettableFuture;
import kws.panier.front.api.ResponsePayload;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;


public class InMemoryRequestService implements ImmediateRequestService {

    private final ConcurrentMap<RequestId, SettableFuture<ResponsePayload>> repository;

    public InMemoryRequestService() {
        this.repository = new ConcurrentHashMap<>();
    }

    @Override
    public RequestId register(SettableFuture<ResponsePayload> futureResponse) {
        RequestId requestId = newRequestId();
        repository.put(requestId, futureResponse);
        return requestId;
    }

    @Override
    public void notify(RequestId requestId, ResponsePayload payload) {
        SettableFuture<ResponsePayload> future = repository.get(requestId);
        if (null != future) {
            future.set(payload);
            repository.remove(requestId, future);
        }
    }

    private RequestId newRequestId() {
        RequestId requestId = null;
        while (null == requestId) {
            requestId = RequestId.instance(UUID.randomUUID().toString());
            if (repository.containsKey(requestId)) {
                requestId = null;
            }
        }
        return requestId;
    }

}
