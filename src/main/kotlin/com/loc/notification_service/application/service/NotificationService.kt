package com.loc.notification_service.application.service

import com.loc.notification_service.application.port.NotificationServicePort
import com.loc.notification_service.domain.model.Notification
import com.loc.notification_service.domain.model.OrderNotification
import com.loc.notification_service.domain.service.EmailContentBuilder
import org.springframework.stereotype.Service

@Service
class NotificationService (
    private val notificationServicePort: NotificationServicePort,
    private val emailContentBuilder: EmailContentBuilder
) {
    fun processOrderNotification(orderNotification: OrderNotification) {
        val emailNotification = createEmailFromOrder(orderNotification)

        notificationServicePort.sendNotification(emailNotification)
    }

    private fun createEmailFromOrder(order: OrderNotification): Notification {
        return Notification(
            to = order.customerEmail,
            subject = "Order Confirmation - ${order.orderId}",
            content = emailContentBuilder.buildOrderConfirmationContent(order),
            isHtml = true
        )
    }
}