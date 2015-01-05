package kws.panier.front.api;

import java.util.Objects;


public final class CacheId {

    public static CacheId instance(String value) {
        return new CacheId(value);
    }

    private final String value;

    private CacheId(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 83 * hash + Objects.hashCode(this.value);
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
        final CacheId other = (CacheId) obj;
        return Objects.equals(this.value, other.value);
    }

    @Override
    public String toString() {
        return "CacheId{" + "value=" + value + '}';
    }

}
