package com.loc.notification_service.application.service

import com.loc.notification_service.application.port.NotificationServicePort
import com.loc.notification_service.domain.model.Notification
import com.loc.notification_service.domain.model.OrderNotification
import org.springframework.stereotype.Service

@Service
class NotificationService (
    private val notificationServicePort: NotificationServicePort
) {
    fun processOrderNotification(orderNotification: OrderNotification) {
        val emailNotification = createEmailFromOrder(orderNotification)

        notificationServicePort.sendNotification(emailNotification)
    }

    private fun createEmailFromOrder(order: OrderNotification): Notification {
        return Notification(
            to = order.customerEmail,
            subject = "Order Confirmation - ${order.orderId}",
            content = buildEmailContent(order),
            isHtml = true
        )
    }

    private fun buildEmailContent(order: OrderNotification): String {
        return """
            <html>
            <body>
                <h2>🎉 Order Confirmed!</h2>
                <p>Dear Customer,</p>
                <p>Your order has been confirmed:</p>
                
                <div style="background: #f5f5f5; padding: 15px; margin: 20px 0;">
                    <p><strong>Order ID:</strong> ${order.orderId}</p>
                    <p><strong>Date:</strong> ${order.orderDate}</p>
                    <p><strong>Status:</strong> ✅ Confirmed</p>
                </div>
                
                <p>Thank you for your business!</p>
                <p>Best regards,<br>E-commerce Team</p>
            </body>
            </html>
        """.trimIndent()
    }

}