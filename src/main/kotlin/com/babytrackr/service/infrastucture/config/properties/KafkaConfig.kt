package com.babytrackr.service.infrastucture.config.properties

import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties(KafkaProperties::class) // ConfigurationProperties performs the mapping to spring's bootstrap server host (defined in application.yaml)
class KafkaConfig {
}
