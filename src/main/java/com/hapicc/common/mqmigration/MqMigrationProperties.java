package com.hapicc.common.mqmigration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "mqmigration")
public class MqMigrationProperties {

    private Kafka kafka = new Kafka();

    private Rabbit rabbit = new Rabbit();

    private Profile profile = new Profile();

    public Kafka getKafka() {
        return kafka;
    }

    public void setKafka(Kafka kafka) {
        this.kafka = kafka;
    }

    public Rabbit getRabbit() {
        return rabbit;
    }

    public void setRabbit(Rabbit rabbit) {
        this.rabbit = rabbit;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public static class Kafka {

        private boolean enable = true;

        private String topics = "classpath:migration/mq/kafka/topics.json";

        private short replicationFactor = 1;

        public boolean isEnable() {
            return enable;
        }

        public void setEnable(boolean enable) {
            this.enable = enable;
        }

        public String getTopics() {
            return topics;
        }

        public void setTopics(String topics) {
            this.topics = topics;
        }

        public short getReplicationFactor() {
            return replicationFactor;
        }

        public void setReplicationFactor(short replicationFactor) {
            this.replicationFactor = replicationFactor;
        }
    }

    public static class Rabbit {

        private boolean enable = true;

        private String exchanges = "classpath:migration/mq/rabbitmq/exchanges.json";

        private String queues = "classpath:migration/mq/rabbitmq/queues.json";

        public boolean isEnable() {
            return enable;
        }

        public void setEnable(boolean enable) {
            this.enable = enable;
        }

        public String getExchanges() {
            return exchanges;
        }

        public void setExchanges(String exchanges) {
            this.exchanges = exchanges;
        }

        public String getQueues() {
            return queues;
        }

        public void setQueues(String queues) {
            this.queues = queues;
        }
    }

    public static class Profile {

        private String development = "development";

        public String getDevelopment() {
            return development;
        }

        public void setDevelopment(String development) {
            this.development = development;
        }
    }
}
