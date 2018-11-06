package com.hapicc.controllers.rabbitmq;

import com.hapicc.common.rabbitmq.RabbitMQService;
import com.hapicc.pojo.HapiccJSONResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.HashMap;

@RestController
@RequestMapping("rabbitmq")
public class TestRabbitMQController {

    private static final String test_rabbitmq_hello_exchange = "test_rabbitmq_hello_exchange";
    private static final String test_rabbitmq_hello_routing_key = "hello";

    @Autowired
    private RabbitMQService rabbitMQService;

    @RequestMapping("hello")
    public HapiccJSONResult hello(@RequestParam Integer count) {
        for (int i = 0; i < count; i++) {
            rabbitMQService.sendMessageToConsumer(
                    test_rabbitmq_hello_exchange,
                    test_rabbitmq_hello_routing_key,
                    new HashMap<>(Collections.singletonMap("helloMsg", String.format("Index(%02d): Hello RabbitMQ~~~", i + 1)))
            );
        }
        return HapiccJSONResult.ok();
    }
}
