package kws.panier.front.core.remote;

import kws.panier.front.api.Context;
import kws.panier.front.api.ResponsePayload;


public interface RemoteListener {

    void onResponse(ResponsePayload payload, Context context);

}
