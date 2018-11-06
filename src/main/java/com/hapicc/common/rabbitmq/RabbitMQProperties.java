package com.hapicc.common.rabbitmq;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@ConfigurationProperties(prefix = "rabbitmq")
public class RabbitMQProperties {

    private String host;

    private Integer port;

    private String username;

    private String password;

    private int numThreads = 30;

    private Recovery recovery = new Recovery();

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getNumThreads() {
        return numThreads;
    }

    public void setNumThreads(int numThreads) {
        this.numThreads = numThreads;
    }

    public Recovery getRecovery() {
        return recovery;
    }

    public void setRecovery(Recovery recovery) {
        this.recovery = recovery;
    }

    public static class Recovery {

        /**
         * Enable or disable connection recovery.
         */
        private boolean automatic = true;

        /**
         * Connection recovery interval.
         */
        private Duration interval = Duration.ofMillis(5000);

        public boolean isAutomatic() {
            return automatic;
        }

        public void setAutomatic(boolean automatic) {
            this.automatic = automatic;
        }

        public Duration getInterval() {
            return interval;
        }

        public void setInterval(Duration interval) {
            this.interval = interval;
        }
    }
}
