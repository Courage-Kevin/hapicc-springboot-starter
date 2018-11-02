package com.hapicc.common.kafka;

import java.util.Map;

public interface KafkaMessageProcessor {

    void processKafkaMessage(String key, Map message);
}
