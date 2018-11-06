package com.hapicc.jobs;

import com.hapicc.common.rabbitmq.RabbitMQService;
import com.hapicc.utils.json.JacksonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeoutException;

@Slf4j
@Component
public class TestRabbitMQHelloJob {

    private static final int NUM_CONSUMER = 5;

    private static final String test_rabbitmq_hello_exchange = "test_rabbitmq_hello_exchange";
    private static final String test_rabbitmq_hello_queue = "test_rabbitmq_hello_queue";
    private static final String test_rabbitmq_hello_routing_key = "hello";

    @Autowired
    private RabbitMQService rabbitMQService;

    @Scheduled(initialDelay = 10000, fixedRate = Long.MAX_VALUE)
    public void execute() throws IOException, TimeoutException {
        createConsumerQueue();
    }

    private void createConsumerQueue() throws IOException, TimeoutException {
        for (int i = 0; i < NUM_CONSUMER; i++) {
            rabbitMQService.getMessageFromProducer(
                    test_rabbitmq_hello_exchange,
                    test_rabbitmq_hello_queue,
                    test_rabbitmq_hello_routing_key,
                    this::processMessage
            );
        }
    }

    private void processMessage(Map message) {
        log.info("Start process message: {}", JacksonUtils.obj2Json(message));
    }
}
