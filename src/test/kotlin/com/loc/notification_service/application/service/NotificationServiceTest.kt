package com.loc.notification_service.application.service

import com.loc.notification_service.application.port.NotificationServicePort
import com.loc.notification_service.domain.model.Notification
import com.loc.notification_service.domain.model.OrderNotification
import com.loc.notification_service.domain.service.EmailContentBuilder
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class NotificationServiceTest {
    
    private lateinit var notificationServicePort: NotificationServicePort
    private lateinit var emailContentBuilder: EmailContentBuilder
    private lateinit var notificationService: NotificationService
    
    @BeforeEach
    fun setUp() {
        notificationServicePort = mockk()
        emailContentBuilder = mockk()
        notificationService = NotificationService(notificationServicePort, emailContentBuilder)
    }
    
    @Test
    fun `should process order notification successfully`() {
        // Given
        val orderNotification = OrderNotification(
            orderId = "ORDER-123",
            orderDate = "2024-01-01",
            customerEmail = "customer@test.com"
        )
        
        val expectedEmailContent = "<html>Order confirmation content</html>"
        val notificationSlot = slot<Notification>()
        
        every { emailContentBuilder.buildOrderConfirmationContent(orderNotification) } returns expectedEmailContent
        every { notificationServicePort.sendNotification(capture(notificationSlot)) } returns true
        
        // When
        notificationService.processOrderNotification(orderNotification)
        
        // Then
        verify(exactly = 1) { emailContentBuilder.buildOrderConfirmationContent(orderNotification) }
        verify(exactly = 1) { notificationServicePort.sendNotification(any()) }
        
        val capturedNotification = notificationSlot.captured
        assertEquals("customer@test.com", capturedNotification.to)
        assertEquals("Order Confirmation - ORDER-123", capturedNotification.subject)
        assertEquals(expectedEmailContent, capturedNotification.content)
        assertTrue(capturedNotification.isHtml)
    }
    
    @Test
    fun `should propagate exception when email content builder fails`() {
        // Given
        val orderNotification = OrderNotification("ORDER-123", "2024-01-01", "customer@test.com")
        val expectedException = RuntimeException("Email content generation failed")
        
        every { emailContentBuilder.buildOrderConfirmationContent(orderNotification) } throws expectedException
        
        // When & Then
        val actualException = assertThrows<RuntimeException> {
            notificationService.processOrderNotification(orderNotification)
        }
        
        assertEquals("Email content generation failed", actualException.message)
        verify(exactly = 1) { emailContentBuilder.buildOrderConfirmationContent(orderNotification) }
        verify(exactly = 0) { notificationServicePort.sendNotification(any()) }
    }
    
    @Test
    fun `should propagate exception when notification port fails`() {
        // Given
        val orderNotification = OrderNotification("ORDER-123", "2024-01-01", "customer@test.com")
        val expectedEmailContent = "<html>Content</html>"
        val expectedException = RuntimeException("Email sending failed")
        
        every { emailContentBuilder.buildOrderConfirmationContent(orderNotification) } returns expectedEmailContent
        every { notificationServicePort.sendNotification(any()) } throws expectedException
        
        // When & Then
        val actualException = assertThrows<RuntimeException> {
            notificationService.processOrderNotification(orderNotification)
        }
        
        assertEquals("Email sending failed", actualException.message)
    }
    
    @Test
    fun `should create notification with correct email format`() {
        // Given
        val orderNotification = OrderNotification(
            orderId = "ORDER-456",
            orderDate = "2024-12-25",
            customerEmail = "john.doe@example.com"
        )
        
        val expectedContent = "<html><body>Merry Christmas Order!</body></html>"
        val notificationSlot = slot<Notification>()
        
        every { emailContentBuilder.buildOrderConfirmationContent(orderNotification) } returns expectedContent
        every { notificationServicePort.sendNotification(capture(notificationSlot)) } returns true
        
        // When
        notificationService.processOrderNotification(orderNotification)
        
        // Then
        val notification = notificationSlot.captured
        assertEquals("john.doe@example.com", notification.to)
        assertEquals("Order Confirmation - ORDER-456", notification.subject)
        assertEquals(expectedContent, notification.content)
        assertTrue(notification.isHtml)
    }
} 