package com.loc.notification_service.infrastructure.adapter

import com.loc.notification_service.domain.model.Notification
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import jakarta.mail.internet.MimeMessage
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.mail.MailException
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class EmailServiceAdapterTest {
    
    private lateinit var mailSender: JavaMailSender
    private lateinit var emailServiceAdapter: EmailServiceAdapter
    private lateinit var mimeMessage: MimeMessage
    
    private val fromEmail = "noreply@test.com"
    
    @BeforeEach
    fun setUp() {
        mailSender = mockk()
        mimeMessage = mockk(relaxed = true)
        emailServiceAdapter = EmailServiceAdapter(mailSender, fromEmail)
    }
    
    @Test
    fun `should send notification successfully`() {
        // Given
        val notification = Notification(
            to = "recipient@test.com",
            subject = "Test Subject",
            content = "<html><body>Test Content</body></html>",
            isHtml = true
        )
        
        every { mailSender.createMimeMessage() } returns mimeMessage
        every { mailSender.send(mimeMessage) } returns Unit
        
        // When
        val result = emailServiceAdapter.sendNotification(notification)
        
        // Then
        assertTrue(result)
        verify(exactly = 1) { mailSender.createMimeMessage() }
        verify(exactly = 1) { mailSender.send(mimeMessage) }
    }

    @Test
    fun `should send plain text notification`() {
        // Given
        val notification = Notification(
            to = "recipient@test.com",
            subject = "Plain Text Subject",
            content = "This is plain text content",
            isHtml = false
        )
        
        every { mailSender.createMimeMessage() } returns mimeMessage
        every { mailSender.send(mimeMessage) } returns Unit
        
        // When
        val result = emailServiceAdapter.sendNotification(notification)
        
        // Then
        assertTrue(result)
        verify(exactly = 1) { mailSender.send(mimeMessage) }
    }
    
    @Test
    fun `should handle empty content notification`() {
        // Given
        val notification = Notification(
            to = "recipient@test.com",
            subject = "Empty Content",
            content = "",
            isHtml = true
        )
        
        every { mailSender.createMimeMessage() } returns mimeMessage
        every { mailSender.send(mimeMessage) } returns Unit
        
        // When
        val result = emailServiceAdapter.sendNotification(notification)
        
        // Then
        assertTrue(result)
    }
} 