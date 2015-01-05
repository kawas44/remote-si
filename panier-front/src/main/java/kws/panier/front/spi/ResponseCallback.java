package kws.panier.front.spi;

import kws.panier.front.api.Context;
import kws.panier.front.api.ResponsePayload;


public interface ResponseCallback {

    public enum Result {

        STOP, CONTINUE;

    }

    Result handle(ResponsePayload response, Context context);

}
