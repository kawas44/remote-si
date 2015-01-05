package kws.panier.front.core.cache;

import com.google.common.base.Optional;
import kws.panier.front.api.ResponsePayload;

import java.util.Objects;


public class CacheResult {

    public static CacheResult missing() {
        return new CacheResult(Optional.<ResponsePayload>absent(), false);
    }

    public static CacheResult fresh(ResponsePayload payload) {
        return new CacheResult(Optional.of(payload), true);
    }

    public static CacheResult stale(ResponsePayload payload) {
        return new CacheResult(Optional.of(payload), false);
    }

    private final Optional<ResponsePayload> payload;
    private final boolean fresh;

    private CacheResult(Optional<ResponsePayload> payloadOpt, boolean fresh) {
        this.payload = payloadOpt;
        this.fresh = fresh && payloadOpt.isPresent();
    }

    public Optional<ResponsePayload> getPayload() {
        return payload;
    }

    public boolean isFresh() {
        return fresh;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + Objects.hashCode(this.payload);
        hash = 29 * hash + (this.fresh ? 1 : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final CacheResult other = (CacheResult) obj;
        if (!Objects.equals(this.payload, other.payload)) {
            return false;
        }
        return this.fresh == other.fresh;
    }

    @Override
    public String toString() {
        return "CacheResponse{"
                + "payload=" + payload
                + ", fresh=" + fresh + '}';
    }

}
