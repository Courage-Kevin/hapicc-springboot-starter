package com.hapicc.common.mqmigration.kafka;

import com.hapicc.common.json.JsonHelper;
import com.hapicc.common.mqmigration.MqMigrationProperties;
import com.hapicc.common.mqmigration.SchemaValidator;
import net.logstash.logback.encoder.org.apache.commons.lang.ArrayUtils;
import org.apache.kafka.clients.admin.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Order(1)
@Component
public class KafkaDeclarer implements CommandLineRunner {

    private final Logger log = LoggerFactory.getLogger(KafkaDeclarer.class);

    @Autowired
    private MqMigrationProperties migrationProperties;

    @Autowired
    private Environment environment;

    @Value("${kafka.bootstrap.servers:localhost:9092}")
    private List<String> bootstrapServers;

    @Autowired
    private SchemaValidator<KafkaTopic> topicValidator;

    @Override
    public void run(String... args) throws Exception {
        if (migrationProperties.getKafka().isEnable()) {
            declare();
        }
    }

    private void declare() throws Exception {
        log.info("=== Kafka declare started");

        File file = ResourceUtils.getFile(migrationProperties.getKafka().getTopics());

        KafkaTopic[] topics = JsonHelper.jackson().getMapper().readValue(file, KafkaTopic[].class);

        if (topics == null || topics.length <= 0) {
            log.info("No topic found in the kafka topics file!");
        } else {
            topicValidator.validate(topics);

            log.info("All topics: {}", Stream.of(topics).map(KafkaTopic::getName).collect(Collectors.toList()));

            Properties properties = new Properties();
            properties.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);

            try (AdminClient client = AdminClient.create(properties)) {
                ListTopicsOptions options = new ListTopicsOptions();
                options.listInternal(false);

                ListTopicsResult existingTopics = client.listTopics(options);
                Set<String> existingTopicNames = existingTopics.names().get();

                for (KafkaTopic topic : topics) {
                    createOrModifyTopic(client, existingTopicNames, topic.getName(), topic);
                }
            }
        }

        log.info("=== Kafka declare finished");
    }

    private void createOrModifyTopic(AdminClient client, Set<String> existingTopicNames, String topicName, KafkaTopic topic) throws ExecutionException, InterruptedException {
        boolean developMode = ArrayUtils.contains(environment.getActiveProfiles(), migrationProperties.getProfile().getDevelopment());

        if (existingTopicNames.contains(topicName)) {
            log.info("The topic with name({}) already exists!", topicName);

            if (!developMode) {
                modifyPartitions(client, topicName, topic);
            }
        } else {
            int numPartitions = topic.getNumPartitions();
            if (developMode) {
                numPartitions = 1;
            }

            short replicationFactor = topic.getReplicationFactor() != null ? topic.getReplicationFactor() : migrationProperties.getKafka().getReplicationFactor();

            NewTopic newTopic = new NewTopic(topicName, numPartitions, replicationFactor);
            CreateTopicsResult result = client.createTopics(Collections.singletonList(newTopic));
            result.all().get();
        }
    }

    private void modifyPartitions(AdminClient client, String topicName, KafkaTopic topic) throws ExecutionException, InterruptedException {
        DescribeTopicsResult result = client.describeTopics(Collections.singletonList(topicName));
        Map<String, TopicDescription> topicDescriptionMap = result.all().get();

        TopicDescription topicDescription = topicDescriptionMap.get(topicName);
        int partitionCount = topicDescription.partitions().size();

        log.info("The count of partitions of topic({}): current = {}, new = {}", topicName, partitionCount, topic.getNumPartitions());

        // partitions 数量只能增加，不能减少
        if (topic.getNumPartitions() < partitionCount) {
            log.warn("New partitions count({}) of topic({}) is less than current value({}), cannot modify!", topic.getNumPartitions(), topicName, partitionCount);
        } else if (topic.getNumPartitions() > partitionCount) {
            Map<String, NewPartitions> newPartitionsMap = new HashMap<>();
            newPartitionsMap.put(topicName, NewPartitions.increaseTo(topic.getNumPartitions()));
            client.createPartitions(newPartitionsMap);

            log.info("The count of partitions of topic({}) has been changed: {} -> {}", topicName, partitionCount, topic.getNumPartitions());
        }
    }
}
