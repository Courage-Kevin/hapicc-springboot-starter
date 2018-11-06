package com.hapicc.common.kafka;

import com.hapicc.common.constants.LogConstants;
import com.hapicc.common.json.JsonHelper;
import com.hapicc.common.json.MessageFormatter;
import com.hapicc.common.utils.CommonUtils;
import com.hapicc.common.utils.LogUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
public class KafkaProducerService {

    static final String K_UPSTREAM = "upstream";
    static final String K_REQUEST_ID = "requestId";

    private static final String K_LOG_LEVEL = "_logLevel_";

    @Value("${kafka.disable:false}")
    private boolean kafkaDisable;

    @Value("${kafka.bootstrap.servers:localhost:9092}")
    private String bootstrapServers;

    private String serviceName;

    private MessageFormatter serializer;

    private volatile KafkaProducer<String, String> kafkaProducer;

    private volatile AtomicBoolean needRestart = new AtomicBoolean(false);

    public KafkaProducerService(String serviceName) {
        this(serviceName, JsonHelper.serializer());
    }

    public KafkaProducerService(String serviceName, MessageFormatter serializer) {
        this.serviceName = serviceName;
        this.serializer = serializer;
    }

    @PostConstruct
    public void init() {
        initProducer();
    }

    public void send(String topic, Object key, Map<String, Object> data) {
        if (kafkaDisable) return;

        if (StringUtils.hasText(serviceName)) {
            data.put(K_UPSTREAM, serviceName);
        }

        if (data.get(K_REQUEST_ID) == null) {
            data.put(K_REQUEST_ID,
                    CommonUtils.isMDCValueNotNull(MDC.get(LogConstants.X_REQUEST_ID))
                            ? MDC.get(LogConstants.X_REQUEST_ID)
                            : UUID.randomUUID().toString().replace("-", ""));
        }

        CommonUtils.preProcessMap(data);

        String message;
        try {
            message = serializer.format(data);
        } catch (IOException e) {
            log.error("=== Error occurred while formatting message to json!", e);
            return;
        }

        LogUtils.log(log, data.get(K_LOG_LEVEL), "=== Send kafka message to topic({}): key: {}, message: {}", topic, String.valueOf(key), message);

        final String _topic = topic;
        final String _message = message;

        getProducer().send(new ProducerRecord<>(_topic, String.valueOf(key), message), (metadata, exception) -> {
            if (exception != null) {
                String oldRequestId = MDC.get(LogConstants.X_REQUEST_ID);
                try {
                    MDC.put(LogConstants.X_REQUEST_ID, String.valueOf(data.get(K_REQUEST_ID)));
                    log.error(String.format("=== Failed to send kafka message to topic(%s), message: %s", _topic, _message), exception);

                    if (exception instanceof IllegalStateException) {
                        log.info("=== Retry to restart kafka producer!");
                        needRestart.compareAndSet(false, true);
                    }
                } finally {
                    MDC.put(LogConstants.X_REQUEST_ID, oldRequestId);
                }
            }
        });
    }

    private KafkaProducer<String, String> getProducer() {
        if (needRestart.get()) {
            synchronized (this) {
                if (needRestart.get()) {
                    try {
                        close();
                    } catch (Exception e) {
                        log.warn("=== Error occurred while closing kafka producer before restarting it!", e);
                    }
                    initProducer();
                    needRestart.compareAndSet(true, false);
                }
            }
        }

        return kafkaProducer;
    }

    private void initProducer() {
        if (kafkaDisable) return;

        if (kafkaProducer == null) {
            Properties props = new Properties();
            props.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
            props.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
            props.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
            props.setProperty(ProducerConfig.LINGER_MS_CONFIG, "100");
            props.setProperty(ProducerConfig.COMPRESSION_TYPE_CONFIG, "snappy");
            props.setProperty(ProducerConfig.BATCH_SIZE_CONFIG, "50000");

            props.setProperty(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, "true");
            props.setProperty(ProducerConfig.ACKS_CONFIG, "all");
            kafkaProducer = new KafkaProducer<>(props);
        }
    }

    @PreDestroy
    private void close() {
        if (kafkaDisable || kafkaProducer == null) {
            return;
        }

        kafkaProducer.flush();
        kafkaProducer.close(5, TimeUnit.SECONDS);
    }
}
