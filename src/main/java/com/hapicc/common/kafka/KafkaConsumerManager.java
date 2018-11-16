package com.hapicc.common.kafka;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.hapicc.common.json.JsonHelper;
import com.hapicc.common.json.MessageParser;
import com.hapicc.common.redis.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

@Slf4j
public abstract class KafkaConsumerManager implements KafkaMessageProcessor {

    protected abstract String getTopic();

    protected abstract String getGroupId();

    protected abstract Integer getNumConsumers();

    protected abstract String getBootstrapServers();

    @Value("${kafka.disable:false}")
    protected boolean kafkaDisable;

    private int maxPollRecords = 50;

    private Properties consumerProps;

    private long pollTimeoutMillis = 600;
    private boolean batchProcess = false;

    private MessageParser deserializer;

    private ExecutorService executor;

    private List<KafkaConsumerTemplate> consumers = new ArrayList<>();

    protected KafkaConsumerManager() {
        deserializer = JsonHelper.deserializer();
    }

    protected KafkaConsumerManager(MessageParser deserializer) {
        this.deserializer = deserializer;
    }

    protected KafkaConsumerManager(boolean batchProcess, long pollTimeoutMillis) {
        this();
        this.batchProcess = batchProcess;
        this.pollTimeoutMillis = pollTimeoutMillis;
    }

    protected KafkaConsumerManager(boolean batchProcess, long pollTimeoutMillis, int maxPollRecords) {
        this(batchProcess, pollTimeoutMillis);
        this.maxPollRecords = maxPollRecords;
    }

    protected KafkaConsumerManager(boolean batchProcess, long pollTimeoutMillis, Properties consumerProps) {
        this(batchProcess, pollTimeoutMillis);
        this.consumerProps = consumerProps;
    }

    public void start() {
        if (kafkaDisable) return;

        Assert.hasText(getTopic(), "The topic cannot be empty!");
        Assert.hasText(getGroupId(), "The groupId cannot be empty!");
        Assert.notNull(getNumConsumers(), "The numConsumers cannot be null!");
        Assert.hasText(getBootstrapServers(), "The bootstrapServers cannot be empty!");

        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder().setNameFormat(getGroupId() + "-thread-%d").build();
        executor = Executors.newFixedThreadPool(getNumConsumers(), namedThreadFactory);

        boolean patternSubscribe = false;

        for (int i = 1; i <= getNumConsumers(); i++) {
            log.info("=== Start consumer {} of groupId: {}, topic: {}", i, getGroupId(), getTopic());

            KafkaConsumerTemplate consumer = new KafkaConsumerTemplate(
                    getTopic(),
                    getGroupId(),
                    getNumConsumers(),
                    patternSubscribe,
                    getBootstrapServers(),
                    String.valueOf(maxPollRecords),
                    consumerProps,
                    pollTimeoutMillis,
                    batchProcess,
                    this,
                    deserializer
            );

            consumers.add(consumer);
            executor.submit(consumer);
        }
    }

    public void shutdown() {
        if (kafkaDisable) return;

        for (KafkaConsumerTemplate consumer : consumers) {
            consumer.shutdown();
        }

        executor.shutdown();

        try {
            executor.awaitTermination(5000, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            log.warn("=== Error occurred while shutdown kafka consumers!", e);
        }
    }
}
