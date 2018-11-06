package com.hapicc.common.rabbitmq;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Supplier;

@Configuration
@EnableConfigurationProperties({ RabbitProperties.class, RabbitMQProperties.class })
public class RabbitMQAutoConfiguration {

    @Value("${spring.application.name:}")
    private String serviceName;

    private final RabbitMQProperties properties;

    private final RabbitProperties rabbitProperties;

    public RabbitMQAutoConfiguration(RabbitMQProperties properties, RabbitProperties rabbitProperties) {
        this.properties = properties;
        this.rabbitProperties = rabbitProperties;
    }

    @Bean
    protected ConnectionFactoryService connectionFactoryService() {
        properties.setHost(getProperty(properties::getHost, rabbitProperties::getHost));
        properties.setPort(getProperty(properties::getPort, rabbitProperties::getPort));
        properties.setUsername(getProperty(properties::getUsername, rabbitProperties::getUsername));
        properties.setPassword(getProperty(properties::getPassword, rabbitProperties::getPassword));
        return new ConnectionFactoryService(properties);
    }

    @Bean
    protected RabbitMQPublishService rabbitMQPublishService(ConnectionFactoryService connectionFactoryService) {
        return new RabbitMQPublishService(connectionFactoryService);
    }

    @Bean
    public RabbitMQService rabbitMQService(ConnectionFactoryService connectionFactoryService, RabbitMQPublishService rabbitMQPublishService) {
        return new RabbitMQService(serviceName, connectionFactoryService, rabbitMQPublishService);
    }

    private <T> T getProperty(Supplier<T> property, Supplier<T> defaultValue) {
        T value = property.get();
        return value != null ? value : defaultValue.get();
    }
}
