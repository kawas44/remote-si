package kws.panier.front.core.immediate;

import com.google.common.util.concurrent.SettableFuture;
import kws.panier.front.api.ResponsePayload;


public interface ImmediateRequestService {

    public RequestId register(SettableFuture<ResponsePayload> futureResponse);

    void notify(RequestId requestId, ResponsePayload payload);

}
