package com.hapicc.services.consumer;

import com.hapicc.common.kafka.KafkaConsumerManager;
import com.hapicc.common.redis.RedisService;
import com.hapicc.utils.json.JacksonUtils;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
public class TestKafkaBatchConsumerService extends KafkaConsumerManager {

    @Value("${kafka.testKafkaBatch.topic}")
    @Getter
    String topic;

    @Value("${kafka.testKafkaBatch.groupId}")
    @Getter
    String groupId;

    @Value("${kafka.testKafkaBatch.numConsumers}")
    @Getter
    Integer numConsumers;

    @Value("${kafka.bootstrap.servers}")
    @Getter
    String bootstrapServers;

    public TestKafkaBatchConsumerService() {
        super(true, 2000, 10);
    }

    @Override
    public void processKafkaMessage(String key, Map message) {
        log.info("Start process batch message: {}", JacksonUtils.obj2Json(message));
    }
}
