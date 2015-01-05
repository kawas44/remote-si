package kws.panier.front;

import kws.panier.front.api.CacheId;
import kws.panier.front.api.CacheInfos;
import kws.panier.front.api.CacheWriteStrategies;
import kws.panier.front.api.RequestPayload;
import kws.panier.front.core.Engine;


public class EngineTI {

    Engine engine;

    //TODO
    public void readAValueNoCache() {
        RequestPayload payload = RequestPayload.instance("READ");
        engine.request(payload, CacheWriteStrategies.notCached(), null);

        // check return value
        // check cache status
    }

    //TODO
    public void readAValueCached() {
        RequestPayload payload = RequestPayload.instance("READ");
        CacheId cacheId = CacheId.instance("new-cache-id");
        CacheInfos cacheInfos = CacheInfos.instance(cacheId, 0, 0);
        engine.request(payload, CacheWriteStrategies.useCacheInfos(cacheInfos), null);

        // check return value
        // check cache status
    }

    //TODO
    public void readAValueCacheIdGuessed() {
        //TODO: register cache resolver

        RequestPayload payload = RequestPayload.instance("READ");
        engine.request(payload, CacheWriteStrategies.guessCacheInfos(), null);

        // check return value
        // check cache status
    }

    public void writeAValue() {
        RequestPayload payload = RequestPayload.instance("WRITE");
        engine.request(payload, CacheWriteStrategies.notCached(), null);

        // check return value
        // check cache status
    }

    //TODO
    public void writeAValueExecuteCallback() {
        //TODO: register callback

        RequestPayload payload = RequestPayload.instance("WRITE");
        engine.request(payload, CacheWriteStrategies.notCached(), null);

        // check return value
        // check cache status
        // check callback result
    }

    public void writeAValueCached() {
        RequestPayload payload = RequestPayload.instance("WRITE");
        CacheId cacheId = CacheId.instance("new-cache-id");
        CacheInfos cacheInfos = CacheInfos.instance(cacheId, 0, 0);
        engine.request(payload, CacheWriteStrategies.useCacheInfos(cacheInfos), null);

        // check return value
        // check cache status
    }

    public void writeAValueCacheIdGuessed() {
        RequestPayload payload = RequestPayload.instance("WRITE");
        engine.request(payload, CacheWriteStrategies.guessCacheInfos(), null);

        // check return value
        // check cache status
    }

}
