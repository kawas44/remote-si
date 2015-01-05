package kws.panier.front.core.remote;

import com.google.common.base.Optional;
import kws.panier.front.api.CacheInfos;
import kws.panier.front.api.Context;
import kws.panier.front.api.RequestPayload;
import kws.panier.front.core.immediate.RequestId;
import kws.panier.front.spi.PayloadConverter;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.util.Map;

import static kws.panier.front.core.remote.AmqpConstants.*;

import org.springframework.amqp.core.*;


public class RemoteAmqpService implements RemoteService {

    private final ConnectionFactory connectionFactory;
    private final String exchange;
    private final String routingKey;

    private final PayloadConverter payloadConverter;

    public RemoteAmqpService(
            ConnectionFactory connectionFactory,
            String exchange,
            String routingKey,
            PayloadConverter payloadConverter) {

        this.connectionFactory = connectionFactory;
        this.exchange = exchange;
        this.routingKey = routingKey;
        this.payloadConverter = payloadConverter;
    }

    @Override
    public void send(RequestPayload payload, Context context) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);

        byte[] encode = payloadConverter.requestToBytes(payload);
        MessageProperties messageProperties = toMessageProperties(context);
        Message message = MessageBuilder
                .withBody(encode)
                .andProperties(messageProperties)
                .build();
        template.send(exchange, routingKey, message);
    }

    private MessageProperties toMessageProperties(Context context) {
        MessagePropertiesBuilder builder = MessagePropertiesBuilder.newInstance();

        // standard headers
        builder
                .setContentType(MessageProperties.CONTENT_TYPE_BYTES)
                .setDeliveryMode(MessageDeliveryMode.PERSISTENT);

        // cache headers
        if (context.isCachable()) {
            builder.setHeader(HDR_CACHABLE, "true");

            Optional<CacheInfos> cacheInfosOpt = context.getCacheInfos();
            if (cacheInfosOpt.isPresent()) {
                CacheInfos cacheInfos = cacheInfosOpt.get();
                builder.setHeader(HDR_CACHE_ID, cacheInfos.getCacheId().getValue());
                builder.setHeader(HDR_CACHE_FRESH_DURATION, cacheInfos.getFreshDuration());
                builder.setHeader(HDR_CACHE_STALE_DURATION, cacheInfos.getStaleDuration());
            }
        }

        // requestId headers
        Optional<RequestId> requestId = context.getRequestId();
        if (requestId.isPresent()) {
            builder.setHeader(HDR_REQUEST_ID, requestId.get().getId());
        }

        // user's headers
        Map<String, String> userHeaders = context.getProperties();
        for (Map.Entry<String, String> entry: userHeaders.entrySet()) {
            builder.setHeader(
                    AmqpConstants.HDR_USR_PREFIX + entry.getKey(),
                    entry.getValue());
        }

        return builder.build();
    }

}
