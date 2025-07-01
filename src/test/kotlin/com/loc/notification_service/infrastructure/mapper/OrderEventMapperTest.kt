package com.loc.notification_service.infrastructure.mapper

import com.loc.orderservice.generated.avro.model.OrderCompletedEvent
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class OrderEventMapperTest {
    
    private lateinit var orderEventMapper: OrderEventMapper
    
    @BeforeEach
    fun setUp() {
        orderEventMapper = OrderEventMapper()
    }
    
    @Test
    fun `should map avro event to domain model correctly`() {
        // Given
        val avroEvent = OrderCompletedEvent.newBuilder()
            .setOrderId("AVRO-ORDER-123")
            .setOrderDate("2024-01-15")
            .build()
        
        // When
        val domainModel = orderEventMapper.toDomain(avroEvent)
        
        // Then
        assertEquals("AVRO-ORDER-123", domainModel.orderId)
        assertEquals("2024-01-15", domainModel.orderDate)
        assertEquals("customer@example.com", domainModel.customerEmail) // Hardcoded for now
    }
} 