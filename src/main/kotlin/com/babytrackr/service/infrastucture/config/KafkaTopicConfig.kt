package com.babytrackr.service.infrastucture.config

import com.babytrackr.service.infrastucture.config.properties.KafkaProperties
import org.apache.kafka.clients.admin.NewTopic
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.config.TopicBuilder

@Configuration
class KafkaTopicConfig(
    private val kafkaProperties: KafkaProperties
) {

    @Bean
    fun eventTopic(): NewTopic {
        return TopicBuilder.name(kafkaProperties.topics.babyEvents)
            .partitions(3)
            .replicas(1)
            .build()
    }
}
