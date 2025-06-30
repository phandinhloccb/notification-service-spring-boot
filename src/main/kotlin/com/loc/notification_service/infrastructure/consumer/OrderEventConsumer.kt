// src/main/kotlin/com/loc/notification_service/infrastructure/consumer/OrderEventConsumer.kt
package com.loc.notification_service.infrastructure.consumer

import com.loc.notification_service.application.service.NotificationService
import com.loc.notification_service.infrastructure.mapper.OrderEventMapper
import com.loc.orderservice.generated.avro.model.OrderCompletedEvent
import mu.KotlinLogging
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.support.Acknowledgment
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Component

@Component
class OrderEventConsumer(
    private val notificationService: NotificationService,
    private val orderEventMapper: OrderEventMapper  
) {
    private val logger = KotlinLogging.logger {}

    @KafkaListener(topics = ["\${spring.kafka.input.topic.order-completed}"])
    fun handleOrderCompletedEvent(
        @Payload avroEvent: OrderCompletedEvent,
    ) {
        logger.info { "Received order completed event: ${avroEvent.orderId}" }
        val orderNotification = orderEventMapper.toDomain(avroEvent)
        notificationService.processOrderNotification(orderNotification)
    }
}