package com.hapicc.common.rabbitmq;

import com.hapicc.common.json.MessageFormatter;

import java.util.Map;

public class RabbitMQPublishMessage {

    private String exchange;

    private String routingKey;

    private Map message;

    private MessageFormatter serializer;

    public String getExchange() {
        return exchange;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    public String getRoutingKey() {
        return routingKey;
    }

    public void setRoutingKey(String routingKey) {
        this.routingKey = routingKey;
    }

    public Map getMessage() {
        return message;
    }

    public void setMessage(Map message) {
        this.message = message;
    }

    public MessageFormatter getSerializer() {
        return serializer;
    }

    public void setSerializer(MessageFormatter serializer) {
        this.serializer = serializer;
    }
}
