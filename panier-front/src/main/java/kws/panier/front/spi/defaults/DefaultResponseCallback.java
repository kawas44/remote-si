package kws.panier.front.spi.defaults;

import kws.panier.front.api.Context;
import kws.panier.front.api.ResponsePayload;
import kws.panier.front.spi.ResponseCallback;


public class DefaultResponseCallback implements ResponseCallback {

    @Override
    public ResponseCallback.Result handle(ResponsePayload response,
                                          Context context) {
        return Result.CONTINUE;
    }

}
