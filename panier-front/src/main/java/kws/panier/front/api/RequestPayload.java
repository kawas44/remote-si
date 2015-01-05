package kws.panier.front.api;

import com.google.common.base.Preconditions;


public class RequestPayload {

    public static RequestPayload instance(Object value) {
        return new RequestPayload(Payload.instance(value));
    }

    private final Payload payload;

    private RequestPayload(Payload payload) {
        Preconditions.checkNotNull(payload, "Invalid payload");
        this.payload = payload;
    }

    public Object getValue() {
        return payload.getValue();
    }

}
