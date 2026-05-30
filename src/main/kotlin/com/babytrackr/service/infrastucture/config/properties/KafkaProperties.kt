package com.babytrackr.service.infrastucture.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "babytrackr.kafka")
data class KafkaProperties(
    val bootstrapServers: String,
    val topics: Topics
) {
    data class Topics(
        val babyEvents: String
    )
}
