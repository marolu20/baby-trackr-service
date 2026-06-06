package com.babytrackr.service

import com.babytrackr.service.infrastucture.config.properties.KafkaProperties
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableConfigurationProperties(KafkaProperties::class)
class BabyTrackrApplication

fun main(args: Array<String>) {
    runApplication<BabyTrackrApplication>(*args)
}
