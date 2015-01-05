package kws.panier.front.core.immediate;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

import java.util.Objects;


public final class RequestId {

    public static RequestId instance(String id) {
        return new RequestId(id);
    }

    private final String id;

    private RequestId(String id) {
        Preconditions.checkArgument(Strings.isNullOrEmpty(id), "Invalid request id");
        this.id = id;
    }

    public String getId() {
        return id;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 83 * hash + Objects.hashCode(this.id);
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
        final RequestId other = (RequestId) obj;
        return Objects.equals(this.id, other.id);
    }

    @Override
    public String toString() {
        return "RequestId{" + "id=" + id + '}';
    }

}
