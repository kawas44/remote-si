package kws.panier.front.api;

import com.google.common.base.Preconditions;


public class ResponsePayload {

    public static ResponsePayload instance(Object value) {
        return new ResponsePayload(Payload.instance(value));
    }

    private final Payload payload;

    private ResponsePayload(Payload payload) {
        Preconditions.checkNotNull(payload, "Invalid payload");
        this.payload = payload;
    }

    public Object getValue() {
        return payload.getValue();
    }

}
