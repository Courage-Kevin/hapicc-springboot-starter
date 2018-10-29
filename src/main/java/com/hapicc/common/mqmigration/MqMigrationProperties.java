package com.hapicc.common.mqmigration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "mqmigration")
public class MqMigrationProperties {

    private Kafka kafka = new Kafka();

    private Profile profile = new Profile();

    public Kafka getKafka() {
        return kafka;
    }

    public void setKafka(Kafka kafka) {
        this.kafka = kafka;
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
