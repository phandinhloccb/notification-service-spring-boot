package com.loc.notification_service.application.port

import com.loc.notification_service.domain.model.Notification

interface NotificationServicePort {
    fun sendNotification(notification: Notification): Boolean
}