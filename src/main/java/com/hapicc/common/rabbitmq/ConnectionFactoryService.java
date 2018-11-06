package com.hapicc.common.rabbitmq;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class ConnectionFactoryService {

    private final Logger log = LoggerFactory.getLogger(ConnectionFactoryService.class);

    private RabbitMQProperties properties;

    private ExecutorService threadPool;

    private ConnectionFactory connectionFactory;

    private Connection connection;

    private boolean closed;

    public ConnectionFactoryService(RabbitMQProperties properties) {
        this.properties = properties;
    }

    @PostConstruct
    public Connection getConnection() throws IOException, TimeoutException {
        if (connection == null) {
            log.info("=== Init ConnectionFactoryService");
            threadPool = Executors.newFixedThreadPool(properties.getNumThreads());
            connection = getConnectionFactory().newConnection(threadPool);
        }
        return connection;
    }

    @PreDestroy
    public synchronized void shutdown() throws IOException {
        if (closed) {
            log.info("=== ConnectionFactoryService has been shutdown");
            return;
        }

        closed = true;

        if (connection != null) {
            connection.close();
            threadPool.shutdown();

            try {
                threadPool.awaitTermination(5, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                log.warn("=== Error occurred while shutdown rabbitmq connection pool!", e);
                return;
            }

            log.info("=== ConnectionFactoryService was successfully shutdown");
        }
    }

    private ConnectionFactory getConnectionFactory() {
        if (connectionFactory == null) {
            connectionFactory = new ConnectionFactory();
            connectionFactory.setHost(properties.getHost());
            connectionFactory.setPort(properties.getPort());
            connectionFactory.setUsername(properties.getUsername());
            connectionFactory.setPassword(properties.getPassword());
            connectionFactory.setAutomaticRecoveryEnabled(properties.getRecovery().isAutomatic());
            connectionFactory.setNetworkRecoveryInterval(properties.getRecovery().getInterval().toMillis());
        }
        return connectionFactory;
    }
}
