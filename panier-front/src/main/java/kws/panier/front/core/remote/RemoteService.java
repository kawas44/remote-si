package kws.panier.front.core.remote;

import kws.panier.front.api.Context;
import kws.panier.front.api.RequestPayload;


public interface RemoteService {

    void send(RequestPayload payload, Context context);

}
