package com.hapicc;

import java.util.TimeZone;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import com.hapicc.services.consumer.TestKafkaBatchConsumerService;
import com.hapicc.services.consumer.TestKafkaHelloConsumerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import tk.mybatis.spring.annotation.MapperScan;

@Slf4j
@SpringBootApplication
// 扫描 MyBatis Mapper 包路径
// 注意：此处的 MapperScan 是 tk.mybatis.spring.annotation.MapperScan
@MapperScan(basePackages = { "com.hapicc.mappers" })
// 扫描所有需要的包，包含一些工具包
@ComponentScan(basePackages = { "com.hapicc", "org.n3r.idworker" })
// 开启定时任务
@EnableScheduling
// 开启异步任务
@EnableAsync
// 开启 JPA 审计
@EnableJpaAuditing
public class HapiccApplication {

    public static void main(String[] args) {
        SpringApplication.run(HapiccApplication.class, args);
    }

    @Autowired
    TestKafkaHelloConsumerService testKafkaHelloConsumerService;

    @Autowired
    TestKafkaBatchConsumerService testKafkaBatchConsumerService;

    @PostConstruct
    public void init() {
        log.info("=== Application has been injected!");

        // 设置服务器时区为 UTC 标准时区
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));

        testKafkaHelloConsumerService.start();
        testKafkaBatchConsumerService.start();
    }

    @PreDestroy
    public void destroy() {
        log.info("=== Application will be destroyed!");

        testKafkaHelloConsumerService.shutdown();
        testKafkaBatchConsumerService.shutdown();
    }
}
