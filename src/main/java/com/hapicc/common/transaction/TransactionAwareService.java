package com.hapicc.common.transaction;

import com.hapicc.common.kafka.KafkaProducerService;
import com.hapicc.common.rabbitmq.RabbitMQService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.Map;

@Service
public class TransactionAwareService {

    private final RabbitMQService rabbitMQService;

    private final KafkaProducerService kafkaProducerService;

    public TransactionAwareService(RabbitMQService rabbitMQService, KafkaProducerService kafkaProducerService) {
        this.rabbitMQService = rabbitMQService;
        this.kafkaProducerService = kafkaProducerService;
    }

    public void publishToKafkaTopic(String topic, Object key, Map<String, Object> message) {
        execute(() -> kafkaProducerService.send(topic, key, message));
    }

    public void publishToRabbitExchange(String exchange, String routingKey, Map<String, Object> message) {
        execute(() -> rabbitMQService.sendMessageToConsumer(exchange, routingKey, message));
    }

    public void publishToRabbitQueue(String queue, Map<String, Object> message) {
        execute(() -> rabbitMQService.publishToQueue(queue, message));
    }

    public void execute(Runnable callback) {
        if (TransactionSynchronizationManager.isActualTransactionActive()) {
            TransactionSynchronizationManager.registerSynchronization(
                    new TransactionSynchronizationAdapter() {
                        @Override
                        public void afterCommit() {
                            super.afterCommit();
                            callback.run();
                        }
                    }
            );
        } else {
            callback.run();
        }
    }
}
