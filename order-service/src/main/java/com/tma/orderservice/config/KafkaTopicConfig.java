package com.tma.orderservice.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaTopicConfig {

    @Value("${notification.topic}")
    private String topic;

    @Bean
    NewTopic notification() {
        // topic name, partition numbers, replication number
        return new NewTopic(topic, 2, (short) 1);
    }


}
