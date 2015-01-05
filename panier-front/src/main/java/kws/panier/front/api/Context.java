package kws.panier.front.api;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;
import kws.panier.front.core.immediate.RequestId;

import java.util.HashMap;
import java.util.Map;


public class Context {

    private final Map<String, String> properties;
    private boolean cachable;
    private Optional<CacheInfos> cacheInfos;
    private Optional<RequestId> requestId;


    public Context() {
        properties = new HashMap<>();
        cacheInfos = Optional.absent();
        requestId = Optional.absent();
    }

    public boolean isCachable() {
        return cachable;
    }

    public void setCachable(boolean cachable) {
        this.cachable = cachable;
    }

    public Optional<CacheInfos> getCacheInfos() {
        return cacheInfos;
    }

    public void setCacheInfos(CacheInfos cacheInfos) {
        this.cacheInfos = Optional.fromNullable(cacheInfos);
    }

    public Optional<RequestId> getRequestId() {
        return requestId;
    }

    public void setRequestId(RequestId requestId) {
        this.requestId = Optional.fromNullable(requestId);
    }

    public void putProperties(Map<String, String> m) {
        if (null != m && !m.isEmpty()) {
            properties.putAll(m);
        }
    }

    public void putProperty(String k, String v) {
        properties.put(k, v);
    }

    public String getProperty(String k) {
        return properties.get(k);
    }

    public Map<String, String> getProperties() {
        return ImmutableMap.copyOf(properties);
    }

}
