package com.loc.notification_service.domain.model

data class OrderNotification(
    val orderId: String,
    val orderDate: String,
    val customerEmail: String
)