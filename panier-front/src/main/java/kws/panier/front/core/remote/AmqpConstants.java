package kws.panier.front.core.remote;


public class AmqpConstants {

    public static final String HDR_USR_PREFIX = "__SCM-U-";
    public static final String HDR_PRIV_PREFIX = "__SCM-X-";
    public static final String HDR_CACHABLE = HDR_PRIV_PREFIX + "CACHABLE";
    public static final String HDR_CACHE_ID = HDR_PRIV_PREFIX + "CACHE-ID";

    public static final String HDR_CACHE_FRESH_DURATION
            = HDR_PRIV_PREFIX + "CACHE-FRESH-MILLIS";
    public static final String HDR_CACHE_STALE_DURATION
            = HDR_PRIV_PREFIX + "CACHE-STALE-MILLIS";

    public static final String HDR_REQUEST_ID = HDR_PRIV_PREFIX + "REQUEST-ID";

}
