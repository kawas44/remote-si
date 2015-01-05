package kws.panier.front.core.immediate;

import kws.panier.front.api.ResponsePayload;


public interface ImmediateRequestListener {

    void onImmediateResponse(RequestId requestId, ResponsePayload payload);

}
