package com.hapicc.common.kafka;

import com.hapicc.common.constants.LogConstants;
import com.hapicc.common.json.MessageParser;
import com.hapicc.common.redis.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.errors.WakeupException;
import org.slf4j.MDC;
import redis.clients.jedis.Jedis;

import java.util.*;
import java.util.regex.Pattern;

@Slf4j
public class KafkaConsumerTemplate implements Runnable {

    private String topic;
    private String groupId;
    private int consumerId;

    private boolean patternSubscribe;

    private Properties consumerProps;
    private KafkaConsumer<String, String> consumer;

    private long pollTimeoutMillis;
    private boolean batchProcess;

    private MessageParser deserializer;

    private RedisService redisService;

    private KafkaMessageProcessor processor;

    KafkaConsumerTemplate(
            String topic,
            String groupId,
            int consumerId,
            boolean patternSubscribe,
            String bootstrapServers,
            String maxPollRecords,
            Properties consumerProps,
            long pollTimeoutMillis,
            boolean batchProcess,
            KafkaMessageProcessor processor,
            MessageParser deserializer,
            RedisService redisService
    ) {
        this.topic = topic;
        this.groupId = groupId;
        this.consumerId = consumerId;

        this.patternSubscribe = patternSubscribe;

        this.pollTimeoutMillis = pollTimeoutMillis;
        this.batchProcess = batchProcess;

        this.processor = processor;
        this.deserializer = deserializer;
        this.redisService = redisService;

        Properties props = new Properties();
        props.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.setProperty(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        props.setProperty(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, maxPollRecords);
        props.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        props.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        props.setProperty(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");

        if (consumerProps != null) {
            props.putAll(consumerProps);
        }

        this.consumerProps = props;
        this.consumer = new KafkaConsumer<>(props);
    }

    @Override
    public void run() {
        try {
            subscribe();

            while (true) {
                try {
                    ConsumerRecords<String, String> records = consumer.poll(pollTimeoutMillis);

                    if (batchProcess) {
                        processBatchMessage(records);
                    } else {
                        for (ConsumerRecord<String, String> record : records) {
                            processKafkaMessage(record);
                        }
                    }
                } catch (WakeupException e) {
                    log.debug("=== The consumer had been woke up!");
                    return;
                } catch (IllegalStateException e) {
                    log.warn("=== IllegalStateException occurred!", e);
                    // TODO: Uncomment the following two lines if use the kafka client 0.9.0.1
//                    log.info("=== Reconnect kafka broker since the kafka client 0.9.0.1 bug KAFKA-4669");
//                    reconnectKafka();
                } catch (Exception e) {
                    log.error("=== Unexpected error occurred!", e);
                }
            }
        } finally {
            consumer.close();
        }
    }

    private void subscribe() {
        if (patternSubscribe) {
            consumer.subscribe(Pattern.compile(topic), new ConsumerRebalanceListener() {
                @Override
                public void onPartitionsRevoked(Collection<TopicPartition> partitions) {
                    log.debug("=== {} topic-partitions are revoked from this consumer.", Arrays.toString(partitions.toArray()));
                }

                @Override
                public void onPartitionsAssigned(Collection<TopicPartition> partitions) {
                    log.info("=== {} topic-partitions are assigned to this consumer.", Arrays.toString(partitions.toArray()));
                }
            });
        } else {
            consumer.subscribe(Collections.singletonList(topic));
        }
    }

    private void processKafkaMessage(ConsumerRecord<String, String> record) {
        if (record.value() == null) return;

        String oldUpstream = MDC.get(LogConstants.X_UPSTREAM);
        String oldRequestId = MDC.get(LogConstants.X_REQUEST_ID);
        try {
            Map message = deserializer.parse(record.value(), Map.class);

            MDC.put(LogConstants.X_UPSTREAM, String.valueOf(message.get(KafkaProducerService.K_UPSTREAM)));
            MDC.put(LogConstants.X_REQUEST_ID,
                    message.get(KafkaProducerService.K_REQUEST_ID) != null
                            ? String.valueOf(message.get(KafkaProducerService.K_REQUEST_ID))
                            : String.valueOf(UUID.randomUUID().toString().replace("-", "")));

            log.info("=== The consumer {} in group {} is processing {}", consumerId, groupId, record.key());

            if (shouldProcessMessage(record)) {
                processor.processKafkaMessage(record.key(), message);
            } else {
                log.info("=== Skip to process the message since it had been processed, partition: {}, offset: {}", record.partition(), record.offset());
            }
        } catch (Exception e) {
            log.error("=== Error occurred while processing kafka message!", e);
        } finally {
            MDC.put(LogConstants.X_UPSTREAM, oldUpstream);
            MDC.put(LogConstants.X_REQUEST_ID, oldRequestId);
        }
    }

    private void processBatchMessage(ConsumerRecords<String, String> records) throws Exception {
        Map<String, List<Map>> shuffle = new HashMap<>();
        for (ConsumerRecord<String, String> record : records) {
            if (record.value() == null) continue;

            log.info("=== [batch] The consumer {} in group {} is processing {}", consumerId, groupId, record.key());

            if (shouldProcessMessage(record)) {
                shuffle.computeIfAbsent(record.key(), k -> new ArrayList<>());
                shuffle.get(record.key()).add(deserializer.parse(record.value(), Map.class));
            } else {
                log.info("=== [batch] Skip to process the message since it had been processed, partition: {}, offset: {}", record.partition(), record.offset());
            }
        }

        if (!shuffle.isEmpty()) {
            processor.processKafkaMessage(null, shuffle);
        }
    }

    private boolean shouldProcessMessage(ConsumerRecord record) {
        String key = String.format("kafka-offset-topic-%s", record.key());
        String field = String.format("partition-%d", record.partition());

        final boolean[] shouldProcess = { false };

        redisService.withRedis((Jedis jedis) -> {
            if (jedis.hexists(key, field)) {
                long previousOffset = Long.parseLong(jedis.hget(key, field));
                if (record.offset() > previousOffset) {
                    shouldProcess[0] = true;
                    jedis.hset(key, field, String.valueOf(record.offset()));
                }
            } else {
                shouldProcess[0] = true;
                jedis.hset(key, field, String.valueOf(record.offset()));
            }
        });

        return shouldProcess[0];
    }

    void shutdown() {
        consumer.wakeup();
    }

    private void reconnectKafka() {
        consumer.close();
        consumer = new KafkaConsumer<>(consumerProps);
        subscribe();
    }
}
