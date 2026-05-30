package com.babytrackr.service.infrastucture.messaging.producer

import com.babytrackr.service.infrastucture.model.EventMessage
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service

@Service
class KafkaProducerService(
    private val kafkaTemplate: KafkaTemplate<Long, EventMessage>,
) {

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(KafkaProducerService::class.java)
    }

    fun send(topicName: String, key: Long, value: EventMessage) {
        kafkaTemplate
            .send(topicName, key, value)
            .whenComplete {
                    result, exception ->
                if (exception == null) {
                    logger.info("Message successfully sent to topic ${result?.recordMetadata?.topic()} at the offset ${result?.recordMetadata?.offset()}")
                } else {
                    logger.error(
                        "Failed to publish message to topic ${result.recordMetadata.topic()}",
                        topicName,
                        exception
                    )
                }
            }
    }
}
