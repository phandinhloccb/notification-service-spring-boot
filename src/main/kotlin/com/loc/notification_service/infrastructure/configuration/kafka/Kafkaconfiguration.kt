package com.loc.notification_service.infrastructure.configuration.kafka

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.annotation.EnableKafka

@EnableKafka
@Configuration
class KafkaConsumerConfiguration

@ConfigurationProperties(prefix = "spring.kafka.input.topic")
class KafkaInputTopics(val orderCompleted: String)