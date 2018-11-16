package com.hapicc.services.consumer;

import com.hapicc.common.kafka.KafkaConsumerManager;
import com.hapicc.common.redis.RedisService;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
public class TestKafkaHelloConsumerService extends KafkaConsumerManager {

    @Value("${kafka.testKafkaHello.topic}")
    @Getter
    String topic;

    @Value("${kafka.testKafkaHello.groupId}")
    @Getter
    String groupId;

    @Value("${kafka.testKafkaHello.numConsumers}")
    @Getter
    Integer numConsumers;

    @Value("${kafka.bootstrap.servers}")
    @Getter
    String bootstrapServers;

    @Override
    public void processKafkaMessage(String key, Map message) {
        log.info("The value of message field: {}", message.get("message"));
    }
}
