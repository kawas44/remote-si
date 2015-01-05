package kws.panier.front.api;

import com.google.common.base.Preconditions;

import java.util.Objects;


public final class CacheInfos {

    private final CacheId cacheId;
    private final long freshDuration;
    private final long staleDuration;

    public static CacheInfos instance(CacheId cacheId, long freshDuration,
                                      long staleDuration) {
        return new CacheInfos(cacheId, freshDuration, staleDuration);
    }

    private CacheInfos(CacheId cacheId, long freshDuration, long staleDuration) {
        Preconditions.checkNotNull(cacheId, "Invalid cache id");
        Preconditions.checkArgument(freshDuration > 0, "Invalid cache fresh duration");
        Preconditions.checkArgument(staleDuration >= 0, "Invalid cache stale duration");

        this.cacheId = cacheId;
        this.freshDuration = freshDuration;
        this.staleDuration = staleDuration;
    }

    public CacheId getCacheId() {
        return cacheId;
    }

    public long getFreshDuration() {
        return freshDuration;
    }

    public long getStaleDuration() {
        return staleDuration;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 83 * hash + Objects.hashCode(this.cacheId);
        hash = 83 * hash + (int) (this.freshDuration ^ (this.freshDuration >>> 32));
        hash = 83 * hash + (int) (this.staleDuration ^ (this.staleDuration >>> 32));
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
        final CacheInfos other = (CacheInfos) obj;
        if (!Objects.equals(this.cacheId, other.cacheId)) {
            return false;
        }
        if (this.freshDuration != other.freshDuration) {
            return false;
        }
        return this.staleDuration == other.staleDuration;
    }

    @Override
    public String toString() {
        return "CacheInfos{"
                + "cacheId=" + cacheId
                + ", freshDuration=" + freshDuration
                + ", staleDuration=" + staleDuration + '}';
    }

}
