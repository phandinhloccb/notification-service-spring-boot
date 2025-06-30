package com.loc.notification_service.infrastructure.mapper

import com.loc.notification_service.domain.model.OrderNotification
import com.loc.orderservice.generated.avro.model.OrderCompletedEvent
import org.springframework.stereotype.Component

@Component
class OrderEventMapper {
    fun toDomain(avroEvent: OrderCompletedEvent): OrderNotification {
        return OrderNotification(
            orderId = avroEvent.orderId.toString(),
            orderDate = avroEvent.orderDate.toString(),
            customerEmail = "customer@example.com"
        )
    }
}