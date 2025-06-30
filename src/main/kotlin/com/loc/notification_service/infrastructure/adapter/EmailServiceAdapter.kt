package com.loc.notification_service.infrastructure.adapter

import com.loc.notification_service.application.port.NotificationServicePort
import com.loc.notification_service.domain.model.Notification
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.mail.javamail.JavaMailSender
import mu.KotlinLogging
import org.springframework.stereotype.Component

@Component
class EmailServiceAdapter(
    private val mailSender: JavaMailSender
): NotificationServicePort {
    private val logger = KotlinLogging.logger {}
    override fun sendNotification(notification: Notification): Boolean {

            val message = mailSender.createMimeMessage()
            val helper = MimeMessageHelper(message, true, "UTF-8")
            
            helper.setTo(notification.to)
            helper.setSubject(notification.subject)
            helper.setText(notification.content, notification.isHtml)
            
            mailSender.send(message)
            logger.info("Email sent successfully to: ${notification.to}")
        return true
    }
}