package com.loc.notification_service.domain.service

import com.loc.notification_service.domain.model.OrderNotification
import org.springframework.stereotype.Component

@Component
class EmailContentBuilder {
    
    fun buildOrderConfirmationContent(order: OrderNotification): String {
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
    
    fun buildOrderCancelledContent(order: OrderNotification): String {
        return """
            <html>
            <body>
                <h2>❌ Order Cancelled</h2>
                <p>Dear Customer,</p>
                <p>Your order has been cancelled:</p>
                
                <div style="background: #ffebee; padding: 15px; margin: 20px 0;">
                    <p><strong>Order ID:</strong> ${order.orderId}</p>
                    <p><strong>Date:</strong> ${order.orderDate}</p>
                    <p><strong>Status:</strong> ❌ Cancelled</p>
                </div>
                
                <p>If you have any questions, please contact us.</p>
                <p>Best regards,<br>E-commerce Team</p>
            </body>
            </html>
        """.trimIndent()
    }
} 