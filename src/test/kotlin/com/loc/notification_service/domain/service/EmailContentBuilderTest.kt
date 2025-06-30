package com.loc.notification_service.domain.service

import com.loc.notification_service.domain.model.OrderNotification
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertContains
import kotlin.test.assertTrue

class EmailContentBuilderTest {
    
    private lateinit var emailContentBuilder: EmailContentBuilder
    
    @BeforeEach
    fun setUp() {
        emailContentBuilder = EmailContentBuilder()
    }
    
    @Test
    fun `should build order confirmation content with all required information`() {
        // Given
        val orderNotification = OrderNotification(
            orderId = "ORDER-789",
            orderDate = "2024-06-15",
            customerEmail = "test@example.com"
        )
        
        // When
        val content = emailContentBuilder.buildOrderConfirmationContent(orderNotification)
        
        // Then
        assertTrue(content.contains("<html>"))
        assertTrue(content.contains("</html>"))
        assertContains(content, "🎉 Order Confirmed!")
        assertContains(content, "ORDER-789")
        assertContains(content, "2024-06-15")
        assertContains(content, "✅ Confirmed")
        assertContains(content, "Dear Customer")
        assertContains(content, "Thank you for your business!")
    }
    
    @Test
    fun `should build order cancelled content with cancellation information`() {
        // Given
        val orderNotification = OrderNotification(
            orderId = "ORDER-999",
            orderDate = "2024-03-10",
            customerEmail = "cancelled@example.com"
        )
        
        // When
        val content = emailContentBuilder.buildOrderCancelledContent(orderNotification)
        
        // Then
        assertTrue(content.contains("<html>"))
        assertTrue(content.contains("</html>"))
        assertContains(content, "❌ Order Cancelled")
        assertContains(content, "ORDER-999")
        assertContains(content, "2024-03-10")
        assertContains(content, "❌ Cancelled")
        assertContains(content, "Your order has been cancelled")
    }
    
    @Test
    fun `should handle special characters in order data`() {
        // Given
        val orderNotification = OrderNotification(
            orderId = "ORDER-<script>alert('xss')</script>",
            orderDate = "2024 & special chars",
            customerEmail = "test@example.com"
        )
        
        // When
        val content = emailContentBuilder.buildOrderConfirmationContent(orderNotification)
        
        // Then
        // Content should include the order ID as-is (escaping should be handled at presentation layer)
        assertContains(content, "ORDER-<script>alert('xss')</script>")
        assertContains(content, "2024 & special chars")
    }
} 