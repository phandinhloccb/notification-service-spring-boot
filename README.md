# Notification Service - Clean Architecture Implementation

A Spring Boot microservice for email notifications built following Clean Architecture principles, implementing the Ports & Adapters pattern with proper dependency inversion. This service consumes Kafka events and sends email notifications without requiring HTTP endpoints.

## 📋 Table of Contents

- [Architecture Overview](#architecture-overview)
- [Project Structure](#project-structure)
- [Clean Architecture Layers](#clean-architecture-layers)
- [Key Features](#key-features)
- [Technologies Used](#technologies-used)
- [Getting Started](#getting-started)
- [Event-Driven Architecture](#event-driven-architecture)
- [Testing Strategy](#testing-strategy)
- [Exception Handling](#exception-handling)
- [Configuration](#configuration)

## 🏗️ Architecture Overview

This project implements Clean Architecture with the following core principles:

- **Dependency Inversion**: Inner layers define interfaces, outer layers implement them
- **Separation of Concerns**: Each layer has a single responsibility
- **Framework Independence**: Business logic is isolated from frameworks
- **Testability**: Easy to unit test with proper mocking
- **Event-Driven**: Consumes Kafka events for asynchronous processing

### Architecture Diagram

```
┌─────────────────────────────────────────────────────────────┐
│                    Kafka Consumer                           │
│              (Event-Driven Interface)                       │
└─────────────────────┬───────────────────────────────────────┘
                      │
┌─────────────────────▼───────────────────────────────────────┐
│                   Application                               │
│           (Use Cases & Business Rules)                      │
└─────────────────────┬───────────────────────────────────────┘
                      │
┌─────────────────────▼───────────────────────────────────────┐
│                     Domain                                  │
│              (Entities & Business Logic)                    │
└─────────────────────┬───────────────────────────────────────┘
                      │
┌─────────────────────▼───────────────────────────────────────┐
│                 Infrastructure                              │
│           (Email Service, Kafka, Configuration)            │
└─────────────────────────────────────────────────────────────┘
```

## 📁 Project Structure

```
src/main/kotlin/com/loc/notification_service/
├── domain/                          # Enterprise Business Rules
│   ├── model/
│   │   ├── Notification.kt         # Domain Entity
│   │   └── OrderNotification.kt    # Domain Value Object
│   └── service/
│       └── EmailContentBuilder.kt  # Domain Service
│
├── application/                     # Application Business Rules
│   ├── service/
│   │   └── NotificationService.kt  # Use Case Implementation
│   └── port/
│       └── NotificationServicePort.kt  # Output Port Contract
│
├── infrastructure/                  # Frameworks & Drivers
│   ├── adapter/
│   │   └── EmailServiceAdapter.kt  # Email Service Implementation
│   ├── consumer/
│   │   └── OrderEventConsumer.kt   # Kafka Event Consumer
│   ├── mapper/
│   │   └── OrderEventMapper.kt     # Event ↔ Domain Mapping
│   ├── configuration/
│   │   └── kafka/
│   │       └── KafkaConfiguration.kt  # Kafka Configuration
│   └── exception/
│       └── GlobalExceptionHandler.kt # Exception Handling
│
└── exception/                       # Cross-cutting Concerns
    └── NotificationException.kt    # Domain Exceptions
```

## 🎯 Clean Architecture Layers

### 1. Domain Layer (Innermost)
- **Purpose**: Contains enterprise business rules and entities
- **Dependencies**: None (pure business logic)
- **Components**:
  - `Notification`: Core domain entity representing an email notification
  - `OrderNotification`: Value object for order-related notifications
  - `EmailContentBuilder`: Domain service for building email content

### 2. Application Layer
- **Purpose**: Contains application-specific business rules (use cases)
- **Dependencies**: Only depends on Domain layer
- **Components**:
  - `NotificationService`: Orchestrates notification processing
  - `NotificationServicePort`: Port (interface) for notification delivery

### 3. Infrastructure Layer (Outermost)
- **Purpose**: Contains frameworks, external services, and technical concerns
- **Dependencies**: Implements interfaces from inner layers
- **Components**:
  - `EmailServiceAdapter`: Implements notification port using Spring Mail
  - `OrderEventConsumer`: Kafka consumer for order completion events
  - `OrderEventMapper`: Maps Kafka events to domain objects
  - `KafkaConfiguration`: Kafka consumer configuration

## ✨ Key Features

- **Clean Architecture Implementation**: Proper dependency inversion and layer separation
- **Event-Driven Architecture**: Kafka integration for consuming order events
- **Email Service Integration**: SMTP email sending with HTML support
- **Avro Schema Evolution**: Type-safe event consumption using Avro schemas
- **Comprehensive Exception Handling**: Layered exception handling with proper logging
- **Background Processing**: Runs as a background service without HTTP endpoints
- **Comprehensive Testing**: Unit and integration tests for all layers

## 🛠️ Technologies Used

- **Framework**: Spring Boot 3.x
- **Language**: Kotlin
- **Messaging**: Apache Kafka with Avro serialization
- **Email**: Spring Mail with SMTP
- **Schema Registry**: Confluent Schema Registry
- **Serialization**: Apache Avro
- **Testing**: JUnit 5, MockK, Spring Kafka Test
- **Build Tool**: Gradle with Kotlin DSL
- **Logging**: Kotlin Logging

## 🚀 Getting Started

### Prerequisites

- JDK 17 or higher
- Docker and Docker Compose (for Kafka, Schema Registry)
- Gradle 7.x or higher
- SMTP server (Mailtrap for development)

### Infrastructure Setup

1. **Start Kafka and Schema Registry**:
   ```bash
   docker-compose up -d kafka zookeeper schema-registry
   ```

2. **Verify Kafka is running**:
   ```bash
   docker-compose ps
   ```

### Running the Application

1. **Build the application**:
   ```bash
   ./gradlew build
   ```

2. **Generate Avro classes**:
   ```bash
   ./gradlew generateAvroJava
   ```

3. **Run the application**:
   ```bash
   ./gradlew bootRun
   ```

### Configuration

The application uses `application.yml` for configuration:

```yaml
spring:
  application:
    name: notification-service
  
  # Kafka Configuration
  kafka:
    bootstrap-servers: localhost:9092
    consumer:
      key-deserializer: org.apache.kafka.common.serialization.StringDeserializer
      value-deserializer: io.confluent.kafka.serializers.KafkaAvroDeserializer
      group-id: notification-service
      properties:
        schema.registry.url: http://localhost:8085
        specific.avro.reader: true
    input:
      topic:
        order-completed: orders.order.completed.v1
  
  # Email Configuration
  mail:
    host: sandbox.smtp.mailtrap.io
    port: 587
    username: your-username
    password: your-password
    from: "noreply@yourcompany.com"
    properties:
      mail:
        smtp:
          auth: true
          starttls:
            enable: true
            required: true
```

## 📡 Event-Driven Architecture

The service consumes Kafka events and processes them asynchronously:

### Event Flow

```
┌─────────────────┐    ┌──────────────────┐    ┌─────────────────┐
│   Order Service │    │ Kafka Topic      │    │ Notification    │
│                 │    │                  │    │ Service         │
│ Publishes       │───▶│ order.completed  │───▶│ Consumes        │
│ OrderCompleted  │    │                  │    │ & Sends Email   │
└─────────────────┘    └──────────────────┘    └─────────────────┘
```

### Event Consumer Implementation

```kotlin
@Component
class OrderEventConsumer(
    private val notificationService: NotificationService,
    private val orderEventMapper: OrderEventMapper
) {
    @KafkaListener(
        topics = ["#{kafkaInputTopics.orderCompleted}"],
        groupId = "notification-service"
    )
    fun consumeOrderCompletedEvent(
        @Payload orderCompletedEvent: OrderCompletedEvent
    ) {
        logger.info { "Received order completed event: ${orderCompletedEvent.orderId}" }
        
        val orderNotification = orderEventMapper.toOrderNotification(orderCompletedEvent)
        notificationService.processOrderNotification(orderNotification)
        
        logger.info { "Successfully processed notification for order: ${orderCompletedEvent.orderId}" }
    }
}
```

### Avro Schema

The service uses Avro schemas for type-safe event consumption:

```json
{
  "type": "record",
  "name": "OrderCompletedEvent",
  "namespace": "com.loc.orderservice.generated.avro.model",
  "fields": [
    {"name": "orderId", "type": "string"},
    {"name": "customerId", "type": "string"},
    {"name": "orderDate", "type": "string"},
    {"name": "customerEmail", "type": "string"}
  ]
}
```

## 🧪 Testing Strategy

### Test Structure Following Clean Architecture

```
src/test/kotlin/com/loc/notification_service/
├── application/service/
│   └── NotificationServiceTest.kt           # Use Case Tests
├── domain/service/
│   └── EmailContentBuilderTest.kt           # Domain Service Tests
├── infrastructure/
│   ├── adapter/
│   │   └── EmailServiceAdapterTest.kt       # Email Service Tests
│   ├── consumer/
│   │   └── OrderEventConsumerTest.kt        # Kafka Consumer Tests
│   └── mapper/
│       └── OrderEventMapperTest.kt          # Event Mapping Tests
└── integration/
    └── NotificationServiceIntegrationTest.kt # End-to-End Tests
```

### Test Categories

1. **Unit Tests**: Test individual components in isolation
2. **Integration Tests**: Test component interactions
3. **Consumer Tests**: Test Kafka event consumption with embedded Kafka
4. **Email Tests**: Test email sending with mock SMTP server

### Running Tests

```bash
# Run all tests
./gradlew test

# Run specific test categories
./gradlew test --tests "*Service*"
./gradlew test --tests "*Consumer*"
./gradlew test --tests "*Integration*"
```

## 🚨 Exception Handling

The application implements a layered exception handling strategy suitable for background services:

### Exception Flow

```
┌─────────────────┐    ┌──────────────────┐    ┌─────────────────┐
│   Kafka Event  │    │   Application    │    │   Infrastructure│
│                 │    │                  │    │                 │
│ Event Consumed  │───▶│ Business Logic   │───▶│ Email Service   │
└─────────────────┘    └──────────────────┘    └─────────────────┘
         │                       │                      │
         │              ┌────────▼────────┐             │
         │              │ Domain          │             │
         │              │ Exceptions      │             │
         │              │ • Business      │             │
         │              │ • Validation    │             │
         │              └─────────────────┘             │
         │                                              │
         │              ┌─────────────────┐             │
         │              │ Infrastructure  │◀────────────┘
         │              │ Exceptions      │
         │              │ • Email Sending │
         │              │ • Network       │
         │              │ • Configuration │
         │              └─────────────────┘
         │                       │
         ▼                       ▼
┌─────────────────────────────────────────┐
│        Exception Handler                │
│                                         │
│ • Log errors with context              │
│ • Retry mechanisms for transient errors│
│ • Dead letter queue for failed events  │
│ • Monitoring and alerting              │
└─────────────────────────────────────────┘
```

### Exception Types

- **Domain Exceptions**: Business rule violations (invalid email format, missing data)
- **Infrastructure Exceptions**: External service failures (email server down, network issues)
- **Event Processing Exceptions**: Kafka consumption errors, deserialization failures

## 📊 Clean Architecture Benefits Achieved

1. **Independence**: Business logic is independent of frameworks, email providers, and message brokers
2. **Testability**: Easy to test business rules without Kafka, email servers, or external services
3. **Flexibility**: Easy to change email providers, message brokers, or event formats
4. **Maintainability**: Clear separation of concerns makes the code easier to understand and modify
5. **Scalability**: Architecture supports scaling individual components independently
6. **Reliability**: Proper error handling and retry mechanisms for robust event processing

## 🔄 Event Processing Flow

The complete flow from event consumption to email delivery:

1. **Event Reception**: Kafka consumer receives `OrderCompletedEvent`
2. **Event Mapping**: Convert Avro event to domain `OrderNotification`
3. **Business Processing**: Application service processes the notification
4. **Content Generation**: Domain service builds email content
5. **Email Delivery**: Infrastructure adapter sends email via SMTP
6. **Error Handling**: Any failures are logged and handled appropriately

## 📈 Monitoring and Observability

The service includes built-in observability features:

- **Structured Logging**: Comprehensive logging with correlation IDs
- **Spring Boot Actuator**: Health checks and metrics endpoints
- **Kafka Consumer Metrics**: Built-in Kafka consumer monitoring
- **Email Delivery Tracking**: Success/failure logging for email operations

## 🚀 Deployment

The service is designed to run as a background worker:

- **No HTTP endpoints**: Pure event-driven processing
- **Stateless**: No local state, easy to scale horizontally
- **Container-ready**: Can be deployed in Docker containers
- **Cloud-native**: Suitable for Kubernetes deployment

---

This notification service demonstrates a clean, maintainable, and testable implementation of Clean Architecture principles in a real-world event-driven microservice. 