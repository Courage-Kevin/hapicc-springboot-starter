package com.hapicc.common.mqmigration.rabbit;

import com.hapicc.common.json.JsonHelper;
import com.hapicc.common.mqmigration.MqMigrationProperties;
import com.hapicc.common.mqmigration.SchemaValidator;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.connection.Connection;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Order(2)
@Component
public class RabbitDeclarer implements CommandLineRunner {

    private final Logger log = LoggerFactory.getLogger(RabbitDeclarer.class);

    @Autowired
    private MqMigrationProperties migrationProperties;

    @Autowired
    private ConnectionFactory connectionFactory;

    @Autowired
    private SchemaValidator<RabbitExchange> exchangeValidator;

    @Autowired
    private SchemaValidator<RabbitQueue> queueValidator;

    @Override
    public void run(String... args) throws Exception {
        if (migrationProperties.getRabbit().isEnable()) {
            declare();
        }
    }

    private void declare() throws Exception {
        log.info("=== Rabbit declare started");

        File exchangesFile = ResourceUtils.getFile(migrationProperties.getRabbit().getExchanges());
        RabbitExchange[] exchanges = JsonHelper.jackson().getMapper().readValue(exchangesFile, RabbitExchange[].class);

        File queuesFile = ResourceUtils.getFile(migrationProperties.getRabbit().getQueues());
        RabbitQueue[] queues = JsonHelper.jackson().getMapper().readValue(queuesFile, RabbitQueue[].class);

        validate(exchanges, queues);

        try (
                Connection connection = connectionFactory.createConnection();
                Channel channel = connection.createChannel(true)
        ) {
            declareExchanges(channel, exchanges);
            declareAndBindQueues(channel, exchanges, queues);
        }

        log.info("=== Rabbit declare finished");
    }

    /**
     * 在 Broker 中创建 Queue 并绑定到 Exchange
     *
     * @param channel   RabbitMQ Channel
     * @param exchanges Exchange 数组
     * @param queues    Queue 数组
     */
    private void declareAndBindQueues(Channel channel, RabbitExchange[] exchanges, RabbitQueue[] queues) throws IOException {
        // 获得 Exchange 的 name-type 映射
        Map<String, String> exchangeTypeMap = Stream.of(exchanges).collect(Collectors.toMap(
                RabbitExchange::getName, RabbitExchange::getType, (oldValue, newValue) -> newValue
        ));

        for (RabbitQueue queue : queues) {
            channel.queueDeclare(
                    queue.getName(),
                    queue.isDurable(),
                    queue.isExclusive(),
                    queue.isAutoDelete(),
                    null
            );

            if (queue.getBindings() != null) {
                for (RabbitQueue.Binding binding : queue.getBindings()) {

                    String exchangeName = binding.getExchange();

                    // Fanout 类型的 Exchange 通常用于绑定匿名 Queue
                    if (exchangeTypeMap.get(exchangeName).equalsIgnoreCase("FANOUT")) {
                        log.info("Skip bind queue({}) with fanout exchange({})", queue.getName(), exchangeName);
                        continue;
                    }

                    channel.queueBind(
                            queue.getName(),
                            binding.getExchange(),
                            binding.getRoutingKey()
                    );
                }
            }
        }
    }

    /**
     * 在 Broker 中创建 Exchange
     *
     * @param channel   RabbitMQ Channel
     * @param exchanges Exchange 数组
     */
    private void declareExchanges(Channel channel, RabbitExchange[] exchanges) throws IOException {
        for (RabbitExchange exchange : exchanges) {
            channel.exchangeDeclare(
                    exchange.getName(),
                    exchange.getType().toLowerCase(),
                    exchange.isDurable(),
                    exchange.isAutoDelete(),
                    null
            );
        }
    }

    /**
     * 检查 Exchange 和 Queue 是否设置正确
     *
     * @param exchanges Exchange 数组
     * @param queues    Queue 数组
     * @throws Exception 验证非法抛出异常
     */
    private void validate(RabbitExchange[] exchanges, RabbitQueue[] queues) throws Exception {
        // 检查字段类型是否合法
        exchangeValidator.validate(exchanges);
        queueValidator.validate(queues);

        // 检查 Exchange 的 name 是否有重复
        String duplicatedExchangeNames = Stream.of(exchanges)
                .collect(Collectors.toMap(RabbitExchange::getName, exchange -> 1, (v1, v2) -> v1 + v2))
                .entrySet().stream()
                .filter(entry -> entry.getValue() > 1)
                .map(Map.Entry::getKey)
                .collect(Collectors.joining(", "));
        if (duplicatedExchangeNames.length() > 0) {
            throw new IllegalArgumentException(String.format("Found duplicated exchanges: %s", duplicatedExchangeNames));
        }

        // 检查 Queue 的 name 是否有重复
        String duplicatedQueueNames = Stream.of(queues)
                .collect(Collectors.toMap(RabbitQueue::getName, queue -> 1, (v1, v2) -> v1 + v2))
                .entrySet().stream()
                .filter(entry -> entry.getValue() > 1)
                .map(Map.Entry::getKey)
                .collect(Collectors.joining(", "));
        if (duplicatedQueueNames.length() > 0) {
            throw new IllegalArgumentException(String.format("Found duplicated queues: %s", duplicatedQueueNames));
        }

        // 检查 Exchange 的 type 是否合法
        String invalidExchanges = Stream.of(exchanges)
                .filter(exchange -> !validateExchangeType(exchange.getType()))
                .map(RabbitExchange::getName)
                .collect(Collectors.joining(", "));
        if (invalidExchanges.length() > 0) {
            throw new IllegalArgumentException(String.format("Found invalid type in exchanges: %s", invalidExchanges));
        }

        // 检查 queues.json 中引用的 Exchange 是否已在 exchanges.json 中定义
        Set<String> declaredExchanges = Stream.of(exchanges)
                .map(RabbitExchange::getName).collect(Collectors.toSet());
        String undeclaredExchanges = Stream.of(queues)
                .filter(queue -> queue.getBindings() != null)
                .flatMap(queue -> queue.getBindings().stream())
                .filter(binding -> !declaredExchanges.contains(binding.getExchange()))
                .map(RabbitQueue.Binding::getExchange)
                .distinct()
                .collect(Collectors.joining(", "));
        if (undeclaredExchanges.length() > 0) {
            throw new IllegalArgumentException(String.format("Found queues binding to but undeclared exchanges: %s", undeclaredExchanges));
        }
    }

    /**
     * 检查 Exchange 的 type 是否合法
     *
     * @param type Exchange 的 type
     * @return true 合法；false 非法
     */
    private boolean validateExchangeType(String type) {
        if (!StringUtils.hasText(type)) {
            return false;
        }

        return Stream.of("DIRECT", "TOPIC", "FANOUT").anyMatch(type::equalsIgnoreCase);
    }
}
