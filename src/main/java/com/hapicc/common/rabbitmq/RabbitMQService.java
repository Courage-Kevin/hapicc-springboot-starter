package com.hapicc.common.rabbitmq;

import com.hapicc.common.constants.LogConstants;
import com.hapicc.common.json.JsonHelper;
import com.hapicc.common.json.MessageFormatter;
import com.hapicc.common.json.MessageParser;
import com.hapicc.common.utils.CommonUtils;
import com.hapicc.common.utils.LogUtils;
import com.rabbitmq.client.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeoutException;
import java.util.function.Consumer;

public class RabbitMQService {

    private final Logger log = LoggerFactory.getLogger(RabbitMQService.class);

    static final String K_UPSTREAM = "upstream";
    static final String K_REQUEST_ID = "requestId";

    static final String K_LOG_LEVEL = "_logLevel_";

    private static final String K_ROUTING_KEY = "__ROUTING_KEY__";

    private static final int DEFAULT_PREFETCH_COUNT = 5;

    private final String serviceName;

    private final ConnectionFactoryService connectionFactoryService;

    private final RabbitMQPublishService rabbitMQPublishService;

    private MessageFormatter serializer;

    private MessageParser deserializer;

    public RabbitMQService(String serviceName, ConnectionFactoryService connectionFactoryService, RabbitMQPublishService rabbitMQPublishService) {
        this(serviceName, connectionFactoryService, rabbitMQPublishService, JsonHelper.serializer(), JsonHelper.deserializer());
    }

    public RabbitMQService(String serviceName, ConnectionFactoryService connectionFactoryService, RabbitMQPublishService rabbitMQPublishService, MessageFormatter serializer, MessageParser deserializer) {
        this.serviceName = serviceName;
        this.connectionFactoryService = connectionFactoryService;
        this.rabbitMQPublishService = rabbitMQPublishService;
        this.serializer = serializer;
        this.deserializer = deserializer;
    }

    /**
     * 发送消息到非 default exchange
     */
    public void sendMessageToConsumer(String exchange, String routingKey, Map<String, Object> data) {
        sendMessage(exchange, null, routingKey, data);
    }

    /**
     * 发送消息到 default exchange
     */
    public void publishToQueue(String queue, Map<String, Object> data) {
        Assert.hasText(queue, "The queue cannot be empty!");
        sendMessage(null, queue, null, data);
    }

    private void sendMessage(String exchange, String queue, String routingKey, Map<String, Object> data) {
        if (StringUtils.hasText(serviceName)) {
            data.put(K_UPSTREAM, serviceName);
        }

        if (data.get(K_REQUEST_ID) == null) {
            data.put(K_REQUEST_ID,
                    CommonUtils.isMDCValueNotNull(MDC.get(LogConstants.X_REQUEST_ID))
                            ? MDC.get(LogConstants.X_REQUEST_ID)
                            : UUID.randomUUID().toString().replace("-", ""));
        }

        CommonUtils.preProcessMap(data);

        String oldRequestId = MDC.get(LogConstants.X_REQUEST_ID);

        try {
            MDC.put(LogConstants.X_REQUEST_ID, String.valueOf(data.get(K_REQUEST_ID)));

            if (queue == null) {
                rabbitMQPublishService.publishMessage(exchange, routingKey, data, serializer);
            } else {
                rabbitMQPublishService.publishMessage("", queue, data, serializer);
            }
        } finally {
            MDC.put(LogConstants.X_REQUEST_ID, oldRequestId);
        }
    }

    public String getMessageFromAnonymousQueue(String exchange, int prefetchCount, Consumer<Map> consumer) throws IOException, TimeoutException {
        return getMessageFromAnonymousQueue(exchange, prefetchCount, false, false, consumer);
    }

    public String getMessageFromAnonymousQueue(String exchange, int prefetchCount, boolean exclusive, boolean autoAck, Consumer<Map> consumer) throws IOException, TimeoutException {
        Connection connection = connectionFactoryService.getConnection();
        Channel channel = connection.createChannel();

        String queue = channel.queueDeclare().getQueue();
        channel.queueBind(queue, exchange, "");

        channel.basicQos(prefetchCount);

        com.rabbitmq.client.Consumer rabbitmqConsumer = buildConsumer(channel, exchange, queue, null, false, consumer);

        if (exclusive) {
            channel.basicConsume(queue, autoAck, "", false, true, null, rabbitmqConsumer);
        } else {
            channel.basicConsume(queue, autoAck, rabbitmqConsumer);
        }

        return queue;
    }

    /**
     * 订阅非 default exchange 中的消息
     */
    public void getMessageFromProducer(String exchange, String queue, String routingKey, int prefetchCount, boolean exclusive, boolean autoAck, boolean embedKey, Consumer<Map> consumer) throws IOException, TimeoutException {

        log.info("=== Start listening to queue({})", queue);

        Connection connection = connectionFactoryService.getConnection();
        Channel channel = connection.createChannel();

        channel.basicQos(prefetchCount);

        com.rabbitmq.client.Consumer rabbitmqConsumer = buildConsumer(channel, exchange, queue, routingKey, embedKey, consumer);

        if (exclusive) {
            channel.basicConsume(queue, autoAck, "", false, true, null, rabbitmqConsumer);
        } else {
            channel.basicConsume(queue, autoAck, rabbitmqConsumer);
        }
    }

    public void getMessageFromProducer(String exchange, String queue, Consumer<Map> consumer) throws IOException, TimeoutException {
        getMessageFromProducer(exchange, queue, null, DEFAULT_PREFETCH_COUNT, false, false, false, consumer);
    }

    public void getMessageFromProducer(String exchange, String queue, boolean exclusive, Consumer<Map> consumer) throws IOException, TimeoutException {
        getMessageFromProducer(exchange, queue, null, DEFAULT_PREFETCH_COUNT, exclusive, false, false, consumer);
    }

    public void getMessageFromProducer(String exchange, String queue, int prefetchCount, Consumer<Map> consumer) throws IOException, TimeoutException {
        getMessageFromProducer(exchange, queue, null, prefetchCount, false, false, false, consumer);
    }

    public void getMessageFromProducer(String exchange, String queue, int prefetchCount, boolean exclusive, Consumer<Map> consumer) throws IOException, TimeoutException {
        getMessageFromProducer(exchange, queue, null, prefetchCount, exclusive, false, false, consumer);
    }

    public void getMessageFromProducer(String exchange, String queue, int prefetchCount, boolean exclusive, boolean autoAck, boolean embedKey, Consumer<Map> consumer) throws IOException, TimeoutException {
        getMessageFromProducer(exchange, queue, null, prefetchCount, exclusive, autoAck, embedKey, consumer);
    }

    public void getMessageFromProducer(String exchange, String queue, String routingKey, Consumer<Map> consumer) throws IOException, TimeoutException {
        getMessageFromProducer(exchange, queue, routingKey, DEFAULT_PREFETCH_COUNT, false, false, false, consumer);
    }

    public void getMessageFromProducer(String exchange, String queue, String routingKey, boolean exclusive, Consumer<Map> consumer) throws IOException, TimeoutException {
        getMessageFromProducer(exchange, queue, routingKey, DEFAULT_PREFETCH_COUNT, exclusive, false, false, consumer);
    }

    public void getMessageFromProducer(String exchange, String queue, String routingKey, int prefetchCount, Consumer<Map> consumer) throws IOException, TimeoutException {
        getMessageFromProducer(exchange, queue, routingKey, prefetchCount, false, false, false, consumer);
    }

    public void getMessageFromProducer(String exchange, String queue, String routingKey, int prefetchCount, boolean exclusive, Consumer<Map> consumer) throws IOException, TimeoutException {
        getMessageFromProducer(exchange, queue, routingKey, prefetchCount, exclusive, false, false, consumer);
    }

    public void getMessageWithEmbeddedKey(String exchange, String queue, Consumer<Map> consumer) throws IOException, TimeoutException {
        getMessageFromProducer(exchange, queue, null, DEFAULT_PREFETCH_COUNT, false, false, true, consumer);
    }

    public void getMessageWithEmbeddedKey(String exchange, String queue, String routingKey, Consumer<Map> consumer) throws IOException, TimeoutException {
        getMessageFromProducer(exchange, queue, routingKey, DEFAULT_PREFETCH_COUNT, false, false, true, consumer);
    }

    /**
     * 订阅 default exchange 中的消息
     */
    public void receiveFromQueue(String queue, Consumer<Map> consumer) throws IOException, TimeoutException {

        log.info("=== Start listening to queue({})", queue);

        if (!StringUtils.hasText(queue)) {
            return;
        }

        Connection connection = connectionFactoryService.getConnection();
        Channel channel = connection.createChannel();

        channel.basicQos(1);

        channel.basicConsume(
                queue, false,
                buildConsumer(channel, "default", queue, null, false, consumer)
        );
    }

    private com.rabbitmq.client.Consumer buildConsumer(Channel channel, String exchange, String queue, String routingKey, boolean embedKey, Consumer<Map> function) {
        return new DefaultConsumer(channel) {
            @SuppressWarnings("unchecked")
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String oldUpstream = MDC.get(LogConstants.X_UPSTREAM);
                String oldRequestId = MDC.get(LogConstants.X_REQUEST_ID);
                try {
                    Map message = deserializer.parseBytes(body, Map.class);

                    MDC.put(LogConstants.X_UPSTREAM, String.valueOf(message.get(K_UPSTREAM)));
                    MDC.put(LogConstants.X_REQUEST_ID,
                            message.get(K_REQUEST_ID) != null
                                    ? String.valueOf(message.get(K_REQUEST_ID))
                                    : UUID.randomUUID().toString().replace("-", ""));

                    if (embedKey) {
                        message.put(K_ROUTING_KEY, envelope.getRoutingKey());
                    }

                    String key = routingKey != null ? routingKey : "";
                    LogUtils.log(log, message.get(K_LOG_LEVEL), "=== Receive rabbitmq message from exchange: {}, queue: {}, routingKey: {}", exchange, queue, key);

                    function.accept(message);

                    getChannel().basicAck(envelope.getDeliveryTag(), false);
                } catch (Exception e) {
                    log.error("=== Error occurred while handling delivery!", e);
                    getChannel().basicReject(envelope.getDeliveryTag(), false);
                } finally {
                    MDC.put(LogConstants.X_UPSTREAM, oldUpstream);
                    MDC.put(LogConstants.X_REQUEST_ID, oldRequestId);
                }
            }
        };
    }
}
