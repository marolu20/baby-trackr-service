package com.babytrackr.service.infrastucture.config

import com.babytrackr.service.infrastucture.config.properties.KafkaProperties
import com.babytrackr.service.infrastucture.model.EventMessage
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.LongSerializer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.core.DefaultKafkaProducerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.core.ProducerFactory
import org.springframework.kafka.support.serializer.JsonSerializer

@Configuration
class KafkaProducerConfig(
    private val kafkaProperties: KafkaProperties
) {

    @Bean
    fun producerFactory(): ProducerFactory<Long, EventMessage> {
        val configProps: MutableMap<String, Any> = HashMap()

        configProps[ProducerConfig.BOOTSTRAP_SERVERS_CONFIG] = kafkaProperties.bootstrapServers
        configProps[ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG] = LongSerializer::class.java
        configProps[ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG] = JsonSerializer::class.java

        // enables 3 retries with a backoff of 1000 milleseconds (1 second) between retries
        configProps[ProducerConfig.RETRIES_CONFIG] = 3
        configProps[ProducerConfig.RETRY_BACKOFF_MS_CONFIG] = 1000
        return DefaultKafkaProducerFactory(configProps)
    }

    @Bean
    fun kafkaTemplate(): KafkaTemplate<Long, EventMessage> {
        return KafkaTemplate(producerFactory())
    }
}
