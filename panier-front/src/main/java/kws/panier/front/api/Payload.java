package kws.panier.front.api;

import java.util.Objects;


final class Payload {

    static Payload instance(Object value) {
        return new Payload(value);
    }

    private final Object value;

    private Payload(Object value) {
        this.value = value;
    }

    public Object getValue() {
        return value;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 41 * hash + Objects.hashCode(this.value);
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
        final Payload other = (Payload) obj;
        return Objects.equals(this.value, other.value);
    }

    @Override
    public String toString() {
        return "Payload{" + "value=" + value + '}';
    }

}
