package com.loc.notification_service.domain.model

data class Notification(
    val to: String,
    val subject: String,
    val content: String,
    val isHtml: Boolean = true
)