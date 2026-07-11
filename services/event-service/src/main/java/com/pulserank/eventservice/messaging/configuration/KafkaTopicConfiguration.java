package com.pulserank.eventservice.messaging.configuration;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfiguration {

    @Bean
    public NewTopic productEvents() {
        return TopicBuilder
                .name("product-events")
                .partitions(12)
                .replicas(1)
                .build();
    }
}
