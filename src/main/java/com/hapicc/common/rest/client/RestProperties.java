package com.hapicc.common.rest.client;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Component
@ConfigurationProperties(prefix = "rest")
public class RestProperties {

    private Duration connectTimeout = Duration.ofMillis(60000);

    private Duration requestTimeout = Duration.ofMillis(60000);

    private Duration readTimeout = Duration.ofMillis(60000);

    private Connection connection = new Connection();

    private Retry retry = new Retry();

    public Duration getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(Duration connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public Duration getRequestTimeout() {
        return requestTimeout;
    }

    public void setRequestTimeout(Duration requestTimeout) {
        this.requestTimeout = requestTimeout;
    }

    public Duration getReadTimeout() {
        return readTimeout;
    }

    public void setReadTimeout(Duration readTimeout) {
        this.readTimeout = readTimeout;
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public Retry getRetry() {
        return retry;
    }

    public void setRetry(Retry retry) {
        this.retry = retry;
    }

    public static class Connection {

        private Pool pool = new Pool();

        public Pool getPool() {
            return pool;
        }

        public void setPool(Pool pool) {
            this.pool = pool;
        }
    }

    public static class Pool {

        private int maxTotal = 200;

        private int defaultMaxPerRoute = 20;

        public int getMaxTotal() {
            return maxTotal;
        }

        public void setMaxTotal(int maxTotal) {
            this.maxTotal = maxTotal;
        }

        public int getDefaultMaxPerRoute() {
            return defaultMaxPerRoute;
        }

        public void setDefaultMaxPerRoute(int defaultMaxPerRoute) {
            this.defaultMaxPerRoute = defaultMaxPerRoute;
        }
    }

    public static class Retry {

        private int maxAttempts = 3;

        private Duration backOffPeriod = Duration.ofMillis(1000);

        private List<String> hosts = new ArrayList<>();

        public int getMaxAttempts() {
            return maxAttempts;
        }

        public void setMaxAttempts(int maxAttempts) {
            this.maxAttempts = maxAttempts;
        }

        public Duration getBackOffPeriod() {
            return backOffPeriod;
        }

        public void setBackOffPeriod(Duration backOffPeriod) {
            this.backOffPeriod = backOffPeriod;
        }

        public List<String> getHosts() {
            return hosts;
        }

        public void setHosts(List<String> hosts) {
            this.hosts = hosts;
        }
    }
}
