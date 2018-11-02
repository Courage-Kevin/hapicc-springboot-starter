package com.hapicc.controllers.kafka;

import com.hapicc.common.kafka.KafkaProducerService;
import com.hapicc.pojo.HapiccJSONResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("kafka")
public class TestKafkaController {

    @Value("${kafka.testKafkaHello.topic}")
    private String testKafkaHelloTopic;

    @Value("${kafka.testKafkaBatch.topic}")
    private String testKafkaBatchTopic;

    @Autowired
    private KafkaProducerService kafkaProducerService;

    @RequestMapping("hello")
    public HapiccJSONResult hello() {
        Map<String, Object> data = new HashMap<>(Collections.singletonMap("message", "Hello Kafka~~~"));
        kafkaProducerService.send(testKafkaHelloTopic, System.nanoTime(), data);
        return HapiccJSONResult.ok();
    }

    @RequestMapping("testBatch")
    public HapiccJSONResult testBatch(@RequestParam Integer count) {
        Map<String, Object> data = new HashMap<>(Collections.singletonMap("action", "testBatch"));
        for (int i = 0; i < count; i++) {
            data.put("batchId", i + 1);
            kafkaProducerService.send(testKafkaBatchTopic, String.format("testBatch-%d", (i / 10) + 1), data);
        }
        return HapiccJSONResult.ok();
    }
}
