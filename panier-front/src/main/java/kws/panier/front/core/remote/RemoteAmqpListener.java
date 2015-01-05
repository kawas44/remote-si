package kws.panier.front.core.remote;

import com.google.common.base.Optional;
import com.google.common.base.Strings;
import kws.panier.front.api.*;
import kws.panier.front.core.Services;
import kws.panier.front.core.immediate.RequestId;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;

import java.util.Map;
import java.util.Map.Entry;

import static kws.panier.front.core.remote.AmqpConstants.*;


public class RemoteAmqpListener implements MessageListener {

    private final ConnectionFactory connectionFactory;
    private final String exchange;
    private final String routingKey;
    private final RemoteListener remoteListener;

    private final Services provider;

    public RemoteAmqpListener(
            ConnectionFactory connectionFactory,
            String exchange,
            String routingKey,
            Services provider,
            RemoteListener remoteListener) {

        this.connectionFactory = connectionFactory;
        this.exchange = exchange;
        this.routingKey = routingKey;
        this.remoteListener = remoteListener;
        this.provider = provider;
    }

    @Override
    public void onMessage(Message msg) {
        byte[] encoded = msg.getBody();

        ResponsePayload responsePayload
                = provider.getPayloadConverter().responseFromBytes(encoded);

        MessageProperties messageProperties = msg.getMessageProperties();
        Context context = toContext(messageProperties);

        remoteListener.onResponse(responsePayload, context);
    }

    private Context toContext(MessageProperties props) {
        Context context = new Context();
        Map<String, Object> headers = props.getHeaders();

        // set user context
        for (Entry<String, Object> entry: headers.entrySet()) {
            String key = entry.getKey();
            if (key.startsWith(HDR_USR_PREFIX)) {
                String newKey = key.replace(HDR_USR_PREFIX, "");
                context.putProperty(newKey, entry.getValue().toString());
            }
        }

        // set request id context
        Optional<RequestId> requestId = parseRequestId(headers);
        if (requestId.isPresent()) {
            context.setRequestId(requestId.get());
        }

        // set cache context
        context.setCachable(parseCachable(headers));
        if (context.isCachable()) {

            Optional<CacheId> cacheId = parseCacheId(headers);
            if (cacheId.isPresent()) {
                long freshMillis = parseMillis(headers, HDR_CACHE_FRESH_DURATION);
                long staleMillis = parseMillis(headers, HDR_CACHE_STALE_DURATION);

                CacheInfos cacheInfos
                        = CacheInfos.instance(cacheId.get(), freshMillis, staleMillis);
                context.setCacheInfos(cacheInfos);
            }
        }

        return context;
    }

    private boolean parseCachable(Map<String, Object> headers) {
        String cachableStr = (String) headers.get(HDR_CACHABLE);
        return Boolean.parseBoolean(cachableStr);
    }

    private long parseMillis(Map<String, Object> headers, String key) {
        long millis = 0L;
        String millisStr = (String) headers.get(key);
        if (millisStr.matches("[1-9]\\d*")) {
            millis = Long.parseLong(millisStr, 10);
        }
        return millis;
    }

    private Optional<RequestId> parseRequestId(Map<String, Object> headers) {
        String requestIdStr = (String) headers.get(HDR_REQUEST_ID);
        if (!Strings.isNullOrEmpty(requestIdStr)) {
            return Optional.of(RequestId.instance(requestIdStr));
        }
        return Optional.absent();
    }

    private Optional<CacheId> parseCacheId(Map<String, Object> headers) {
        String cacheIdStr = (String) headers.get(HDR_CACHE_ID);
        if (!Strings.isNullOrEmpty(cacheIdStr)) {
            return Optional.of(CacheId.instance(cacheIdStr));
        }
        return Optional.absent();
    }

}
