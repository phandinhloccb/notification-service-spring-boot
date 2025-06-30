package com.loc.notification_service.application.service

import com.loc.notification_service.application.port.NotificationServicePort
import com.loc.notification_service.domain.model.Notification
import org.springframework.stereotype.Service

@Service
class NotificationService (
    private val notificationServicePort: NotificationServicePort
) {
    fun sendNotification() {
        val notification = Notification(
            to = "test@test.com",
            subject = "Test",
            content = "Test"
        )
        notificationServicePort.sendNotification(notification)
    }

}