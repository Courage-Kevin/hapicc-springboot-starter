package com.hapicc.common.rabbitmq;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.hapicc.common.constants.LogConstants;
import com.hapicc.common.json.MessageFormatter;
import com.hapicc.common.utils.LogUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.MessageProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.*;

public class RabbitMQPublishService implements Runnable {

    private final Logger log = LoggerFactory.getLogger(RabbitMQPublishService.class);

    private static final String SHUTDOWN_PUBLISH_SERVICE_EXCHANGE = "shutdown_publish_service_exchange";

    private final ConnectionFactoryService connectionFactoryService;

    private BlockingQueue<RabbitMQPublishMessage> blockingQueue = new LinkedBlockingQueue<>();

    private ExecutorService executor;

    private boolean closed;

    public RabbitMQPublishService(ConnectionFactoryService connectionFactoryService) {
        this.connectionFactoryService = connectionFactoryService;
    }

    @PostConstruct
    public void start() {
        if (executor == null) {
            log.info("=== Init RabbitMQPublishService");

            ThreadFactory namedThreadFactory = new ThreadFactoryBuilder().setNameFormat("rabbitmq-publish-message-thread-%d").build();
            executor = Executors.newFixedThreadPool(1, namedThreadFactory);
            executor.submit(this);
        }
    }

    @Override
    public void run() {
        Connection connection;
        try {
            connection = connectionFactoryService.getConnection();
        } catch (IOException | TimeoutException e) {
            log.error("=== Error occurred while getting rabbitmq connection!", e);
            return;
        }

        try (Channel channel = connection.createChannel()) {
            try {
                while (true) {
                    RabbitMQPublishMessage publishMessage = blockingQueue.take();

                    if (publishMessage.getExchange().equals(SHUTDOWN_PUBLISH_SERVICE_EXCHANGE)) {
                        return;
                    }

                    publish(channel, publishMessage);
                }
            } catch (InterruptedException e) {
                log.debug("=== InterruptedException occurred while waiting to take message!");
            }
        } catch (IOException | TimeoutException e) {
            log.error("=== Error occurred while creating rabbitmq channel!", e);
        }
    }

    private void publish(Channel channel, RabbitMQPublishMessage publishMessage) {
        String message;
        try {
            message = publishMessage.getSerializer().format(publishMessage.getMessage());
        } catch (IOException e) {
            log.error("=== Error occurred while formatting message to json!", e);
            return;
        }

        LogUtils.log(log, publishMessage.getMessage().get(RabbitMQService.K_LOG_LEVEL),
                "=== Publish rabbitmq message, exchange: {}, routingKey: {}, message: {}", publishMessage.getExchange(), publishMessage.getRoutingKey(), message);

        String oldRequestId = MDC.get(LogConstants.X_REQUEST_ID);
        try {
            Object requestId = publishMessage.getMessage().get(RabbitMQService.K_REQUEST_ID);
            MDC.put(LogConstants.X_REQUEST_ID, requestId != null ? String.valueOf(requestId) : "");

            channel.basicPublish(
                    publishMessage.getExchange(),
                    publishMessage.getRoutingKey(),
                    MessageProperties.PERSISTENT_TEXT_PLAIN,
                    message.getBytes(StandardCharsets.UTF_8)
            );
        } catch (Exception e) {
            log.error("=== Error occurred while publishing rabbitmq message!", e);
        } finally {
            MDC.put(LogConstants.X_REQUEST_ID, oldRequestId);
        }
    }

    void publishMessage(String exchange, String routingKey, Map message, MessageFormatter serializer) {
        RabbitMQPublishMessage publishMessage = new RabbitMQPublishMessage();
        publishMessage.setExchange(exchange != null ? exchange : "");
        publishMessage.setRoutingKey(routingKey);
        publishMessage.setMessage(message);
        publishMessage.setSerializer(serializer);
        publishMessage(publishMessage);
    }

    private void publishMessage(RabbitMQPublishMessage publishMessage) {
        try {
            log.debug("=== Start to put message to blocking queue");
            blockingQueue.put(publishMessage);
            log.debug("=== End to put message to blocking queue");
        } catch (InterruptedException e) {
            log.error("=== Error occurred while putting message to blocking queue!", e);
        }
    }

    @PreDestroy
    public synchronized void shutdown() {
        if (closed) {
            log.info("=== RabbitMQPublishService has been shutdown");
            return;
        }

        closed = true;

        RabbitMQPublishMessage shutdownMessage = new RabbitMQPublishMessage();
        shutdownMessage.setExchange(SHUTDOWN_PUBLISH_SERVICE_EXCHANGE);
        publishMessage(shutdownMessage);

        executor.shutdown();

        try {
            executor.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            log.warn("=== Error occurred while shutdown rabbitmq publish service!", e);
            return;
        }

        log.info("=== RabbitMQPublishService was successfully shutdown");
    }
}
